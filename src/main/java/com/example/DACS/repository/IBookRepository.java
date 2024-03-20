package com.example.DACS.repository;

import com.example.DACS.entity.Book;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategoryNameOrderByCreationTimeDesc(String categoryName, PageRequest of);

    //Spring Data JPA sử dụng một quy ước về tên phương thức để xác định cách triển khai câu truy vấn tương ứng.
    List<Book> findByCategoryName(String categoryName);
}

