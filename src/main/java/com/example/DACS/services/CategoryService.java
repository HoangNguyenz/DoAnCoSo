package com.example.DACS.services;

import com.example.DACS.entity.Category;
import com.example.DACS.repository.ICategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public List<Category> getAllCategories(){return categoryRepository.findAll();}

    public Category getCategoryById(Long id){
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()){
            return optionalCategory.get();
        }else {
            throw new RuntimeException("Category not found");
        }
    }


    public Category saveCategory(Category category){return categoryRepository.save(category);}

    public void deleteCategory(Long id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
            categoryRepository.delete(category);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete category due to existing references in books.", e);
        }
    }



    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

}
