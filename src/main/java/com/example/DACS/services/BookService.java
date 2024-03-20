package com.example.DACS.services;

import com.example.DACS.entity.Book;
import com.example.DACS.entity.Category;
import com.example.DACS.repository.IBookRepository;
import com.example.DACS.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private IBookRepository bookRepository;
    private ICategoryRepository categoryService;

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book getBookById(Long id){
        Optional<Book> optional = bookRepository.findById(id);
        return optional.orElse(null);
    }



    public void addBook(Book book){
        bookRepository.save(book);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

    public void updateBook(Book book){
        bookRepository.save(book);
    }

    public List<Book> getBooksByCategoryName(String categoryName, int limit) {
        return bookRepository.findByCategoryNameOrderByCreationTimeDesc(categoryName, PageRequest.of(0, limit));
    }

    public List<Book> getBooksByCategoryName(String categoryName) {
        return bookRepository.findByCategoryName(categoryName);
    }






}
