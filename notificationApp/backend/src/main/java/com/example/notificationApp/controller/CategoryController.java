package com.example.notificationApp.controller;

import com.example.notificationApp.entity.Category;
import com.example.notificationApp.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }


    @GetMapping("/list")
    public List<Category> getCategories(){
        return categoryService.getCategories();
    }


    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable int categoryId){
        categoryService.deleteCategory(categoryId);
    }



}
