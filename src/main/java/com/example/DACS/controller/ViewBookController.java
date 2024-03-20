package com.example.DACS.controller;
import com.example.DACS.entity.Book;
import com.example.DACS.entity.Category;
import com.example.DACS.services.BookService;
import com.example.DACS.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/view")
public class ViewBookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping
    public String home(Model model ) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "viewbook/list";
    }

    @GetMapping("/viewBookByCate/{categoryName}")
    public String viewBooksByCategory(@PathVariable("categoryName") String categoryName, Model model) {
        List<Book> books = bookService.getBooksByCategoryName(categoryName);
        model.addAttribute("books", books);

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "viewbook/bookbycate";
    }


}














