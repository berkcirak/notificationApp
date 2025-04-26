package com.example.notificationApp.service;

import com.example.notificationApp.entity.Category;
import com.example.notificationApp.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository=categoryRepository;
    }

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    public void deleteCategory(int categoryId){
        categoryRepository.deleteById(categoryId);
    }


}
