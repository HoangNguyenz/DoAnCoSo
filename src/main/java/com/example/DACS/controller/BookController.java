package com.example.DACS.controller;

import com.example.DACS.daos.Item;
import com.example.DACS.entity.Book;
import com.example.DACS.services.BookService;
import com.example.DACS.services.CartService;
import com.example.DACS.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;


    @GetMapping
    public String showAllBooks(Model model){
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "book/list";
    }

    @GetMapping("/add")
    public String addBookForm(Model model){
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/add";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult,
                          @RequestParam("image") MultipartFile image, Model model) throws IOException {

//        kiểm tra xem có lỗi validation trên đối tượng "book" hay không.
//        Nếu có, trả về trang "book/add" để hiển thị lại biểu mẫu với thông báo lỗi và
//        danh sách các danh mục sách.
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "book/add";
        }

        // Set giá trị cho trường creationTime
        Date now = Calendar.getInstance().getTime();
        book.setCreationTime(now);

//        Kiểm tra xem tệp hình ảnh đã được chọn hay không.
//        Nếu không rỗng, thực hiện lưu trữ hình ảnh trên máy chủ.
        if (!image.isEmpty()) {
//            Tạo một tên duy nhất cho tệp hình ảnh sử dụng UUID và định dạng ".jpg"
            String imageName = UUID.randomUUID().toString() + ".jpg";
            String uploadDir = "src/main/resources/static/BookImage"; // Đường dẫn tới thư mục lưu trữ hình ảnh trên máy chủ

//            Đọc dữ liệu từ tệp hình ảnh gửi đến từ form thành mảng byte.
            byte[] imageData = image.getBytes();

//            Tạo một đối tượng File mới với đường dẫn và tên tệp hình ảnh.
            File imageFile = new File(uploadDir, imageName);

//            Tạo một đối tượng FileOutputStream để ghi dữ liệu hình ảnh vào tệp.
            FileOutputStream fos = new FileOutputStream(imageFile);

//            Ghi dữ liệu hình ảnh vào tệp.
            fos.write(imageData);
            fos.close();

            book.setImagePath("BookImage/" + imageName); // Lưu đường dẫn tệp hình ảnh vào trong csdl
        }

        bookService.addBook(book);
        return "redirect:/books";
    }


    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            // Handle book not found error
            return "error";
        }
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/edit";
    }

    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, @ModelAttribute("book") Book updatedBook) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            // Handle book not found error
            return "error";
        }
        Date now = Calendar.getInstance().getTime();


        // Update the book with the new values
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setPrice(updatedBook.getPrice());
        book.setCategory(updatedBook.getCategory());
        book.setSoTrang(updatedBook.getSoTrang());
        book.setKhuonKho(updatedBook.getKhuonKho());
        book.setDinhDang(updatedBook.getDinhDang());
        book.setCreationTime(now);
        bookService.updateBook(book);
        return "redirect:/books";
    }



    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session,
                            @RequestParam long id,
                            @RequestParam String name,
                            @RequestParam double price,
                            @RequestParam(defaultValue = "1") int quantity)
    {
        var cart = cartService.getCart(session); //lấy giỏ hàng từ session
        cart.addItems(new Item(id, name, price, quantity));
        cartService.updateCart(session, cart);
        return "redirect:/home/details/" + id;
    }




}



