package com.ecommerce.project.controller;


import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO savedProductDTO = productService.addProcuct(categoryId,productDTO);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                          @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false)  Integer pageSize,
                                                          @RequestParam(defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
                                                          @RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        ProductResponse productResponse = productService.getAllProduct(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getAllProductsByCategory(@PathVariable Long categoryId,
                                                                    @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                    @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false)  Integer pageSize,
                                                                    @RequestParam(defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
                                                                    @RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        ProductResponse productResponse = productService.getProductByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getAllProductsByKeyword(@PathVariable String keyword,
                                                                   @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                   @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false)  Integer pageSize,
                                                                   @RequestParam(defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
                                                                   @RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        ProductResponse productResponse = productService.getProductByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO savedProduct = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(savedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO productDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateImage(productId,image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    }

