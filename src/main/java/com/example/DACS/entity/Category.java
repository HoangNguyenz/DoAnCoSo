package com.example.DACS.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    //Annotation @OneToMany được sử dụng để xác định quan hệ một-nhiều,
    // và mappedBy = "category" chỉ định rằng trường category trong entity Book
    // là trường tham chiếu đến entity Category. Nó cho biết rằng quan hệ một-nhiều
    // được xác định bởi trường category trong entity Book, và bên chủ của quan hệ
    // là entity Category
    @OneToMany(mappedBy = "category")
    private List<Book> books;

    public Category() {
    }

    public Category(Long id, String name, List<Book> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
