package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.path}")
    private String path;

    @Override
    public ProductDTO addProcuct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "categoryId", categoryId)
        );
        boolean isNotPresent = true;
        List<Product> products = category.getProducts();
        for(Product product : products){
            if(product.getProductName().equals(productDTO.getProductName())){
                isNotPresent = false;
                break;
            }
        }
        if(isNotPresent){
            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("default.jpg");
            product.setCategory(category);
            double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscount() * 0.01);
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }else{
            throw new APIException("Product exists");
        }

    }

    @Override
    public ProductResponse getAllProduct() {
        List<Product>  productList = productRepository.findAll();
        List<ProductDTO>  productDTOS = productList.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        if(productList.isEmpty()){
            throw new APIException("Product not found");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "categoryId", categoryId)
        );
        List<Product>  productList = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO>  productDTOS = productList.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        if(productList.isEmpty()){
            throw new APIException("Product not found");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword) {
        List<Product>  productList = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%");
        List<ProductDTO>  productDTOS = productList.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        if(productList.isEmpty()){
            throw new APIException("Product not found");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product productFromDB = productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product", "productId", productId));

        productFromDB.setPrice(productDTO.getPrice());
        productFromDB.setProductName(productDTO.getProductName());
        productFromDB.setQuantity(productDTO.getQuantity());
        productFromDB.setDiscount(productDTO.getDiscount());
        productFromDB.setDescription(productDTO.getDescription());

        Product updatedProduct = productRepository.save(productFromDB);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productFromDB = productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(productFromDB);
        return modelMapper.map(productFromDB, ProductDTO.class);

    }

    @Override
    public ProductDTO updateImage(Long productId, MultipartFile file) throws IOException {
        Product productFromDB = productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path,file);
        productFromDB.setImage(fileName);
        Product updatedProduct = productRepository.save(productFromDB);
        return modelMapper.map(updatedProduct, ProductDTO.class);

    }
}
