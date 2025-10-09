package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.project.payload.CategoryDTO;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/api/public/categories")
    public ResponseEntity<CategoryResponse> getCategoryList(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false)  Integer pageSize,
                                                            @RequestParam(defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
                                                            @RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {

        return new ResponseEntity<>(categoryService.getCategoryList(pageNumber,pageSize,sortBy,sortOrder), HttpStatus.OK);
    }

    @PostMapping("/api/public/categories")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {

        return new ResponseEntity<>(categoryService.addCategory(categoryDTO),HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {

        return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);

    }

    @PutMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);

    }


}
