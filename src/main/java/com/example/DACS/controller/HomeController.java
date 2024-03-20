package com.example.DACS.controller;

import com.example.DACS.entity.Book;
import com.example.DACS.services.BookService;
import com.example.DACS.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping
    public String home(Model model) {
        List<Book> books = bookService.getBooksByCategoryName("Văn Học",8);
        model.addAttribute("books", books);

        List<Book> booksManga = bookService.getBooksByCategoryName("Manga - Comics",4);
        model.addAttribute("booksManga", booksManga);

        List<Book> booksDoraemon = bookService.getBooksByCategoryName("Doraemon",4);
        model.addAttribute("booksDoraemon", booksDoraemon);

        List<Book> booksHSTL = bookService.getBooksByCategoryName("Trinh Thám",4);
        model.addAttribute("booksHSTL", booksHSTL);

        return "home/index";
    }

    @GetMapping("/details/{id}")
    public String Details(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            // Handle book not found error
            return "error";
        }
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryService.getAllCategories());

        List<Book> books = bookService.getBooksByCategoryName("Văn Học",8);
        model.addAttribute("books", books);


        return "home/details";
    }


}
