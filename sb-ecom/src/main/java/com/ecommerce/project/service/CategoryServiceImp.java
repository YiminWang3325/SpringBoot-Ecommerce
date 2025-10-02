package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryResponse getCategoryList(Integer pageNumber, Integer pageSize,String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageDetail = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetail);
        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty()) {
            throw new APIException("No Category found");
        }
        List<CategoryDTO> categoryDTOS = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getNumberOfElements());
        categoryResponse.setLast(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null) {
            throw new APIException("Category with : " + category.getCategoryId() + " already exists");
        }
        Category savedCategorySaved = categoryRepository.save(category);

        return modelMapper.map(savedCategorySaved, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category oldCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId",categoryId));


       categoryRepository.delete(oldCategory);

       return modelMapper.map(oldCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {

        Category oldCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId",categoryId));

        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        Category savedCategory  = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
