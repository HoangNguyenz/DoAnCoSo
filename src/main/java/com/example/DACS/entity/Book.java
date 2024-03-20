package com.example.DACS.entity;

import com.example.DACS.validator.annotation.ValidCategoryId;
import com.example.DACS.validator.annotation.ValidUserId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//@Data tự động tạo các phương thức getter, setter, equals trong thư viện Lombok
@Data
@Entity
@Table(name = "book")
public class Book {
    //GeneratedValue: tự động sinh value cho khóa chính,
    // GenerationType.IDENTITY: giá trị của PK là duy nhất và tự động tăng
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title")
    @NotEmpty(message = "Title must not be empty")
    @Size(max = 100, min = 1, message = "Title mist be less than 50 characters")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name="price")
    @NotNull(message = "Price is required")
    private Double price;

    @Column(name="SoTrang")
    @NotNull(message = "So trang is required")
    private int SoTrang;

    @Column(name="DinhDang")
    @NotNull(message = "Dinh dang is required")
    private String DinhDang;

    @Column(name="KhuonKho")
    @NotNull(message = "Khuon Kho is required")
    private String KhuonKho;

    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    //ManyToOne: Xác định mối quan hệ nhiều-đến-một giữa sách và danh mục (category)
    // và giữa sách và người dùng (user).
    // tạo khóa ngoại tên category_id tham chiếu đến bảng category
    @ManyToOne  //mỗi cuốn sách thuộc về một danh mục duy nhất.
    @JoinColumn(name="category_id", nullable = true)
    @ValidCategoryId
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ValidUserId
    private User user;

    //một trường tạm thời và không được lưu trữ trong cơ sở dữ liệu.
    @Transient
    private MultipartFile image;

    @Column(name = "image_path")
    private String imagePath;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @ToString.Exclude //Bằng cách sử dụng @ToString.Exclude trên một trường dữ liệu cụ thể, chỉ định rằng trường đó sẽ không được bao gồm trong phương thức toString() được tạo ra bởi Lombok.
    private List<ItemInvoice> itemInvoices = new ArrayList<>();

    // ...



    public Book() {
    }

    public Book(Long id, String title, String author, @NotNull(message = "Price is required") Double price, @NotNull(message = "So trang is required") int soTrang, @NotNull(message = "Dinh dang is required") String dinhDang, @NotNull(message = "Khuon Kho is required") String khuonKho, Category category, User user, MultipartFile image, String imagePath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        SoTrang = soTrang;
        DinhDang = dinhDang;
        KhuonKho = khuonKho;
        this.category = category;
        this.user = user;
        this.image = image;
        this.imagePath = imagePath;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getSoTrang() {
        return SoTrang;
    }

    public void setSoTrang(int soTrang) {
        SoTrang = soTrang;
    }

    public String getDinhDang() {
        return DinhDang;
    }

    public void setDinhDang(String dinhDang) {
        DinhDang = dinhDang;
    }

    public String getKhuonKho() {
        return KhuonKho;
    }

    public void setKhuonKho(String khuonKho) {
        KhuonKho = khuonKho;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    //định dạng giá sách thành một chuỗi định dạng số có dấu phân cách hàng nghìn.
    public String getFormattedPrice() {
        //mẫu định dạng số ###,###  ví dụ: 1,000,000
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false; Book book = (Book) o;
        return getId() != null && Objects.equals(getId(), book.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
