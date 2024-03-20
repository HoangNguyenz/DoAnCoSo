package com.example.DACS.daos;


import com.example.DACS.entity.ItemInvoice;
import lombok.Data;
import java.util.ArrayList;

import java.util.List;
import java.util.Objects;
@Data
public class Cart {
    //danh sách các sản phẩm
    private List<Item> cartItems = new ArrayList<>();

    //Phương thức này được sử dụng để thêm một mục mới vào giỏ hàng.
    // Nếu mục đã tồn tại trong giỏ hàng, nó sẽ tăng số lượng của mục đó.
    // Nếu mục chưa tồn tại trong giỏ hàng, nó sẽ được thêm vào danh sách cartItems.
    public void addItems(Item item) {
        boolean isExist = cartItems.stream() //tạo 1 stream
                .filter(i -> Objects.equals(i.getBookId(),  // lọc các mục có bookId trùng khớp với item.getBookId().
                        item.getBookId()))
                .findFirst()
                .map(i -> {
                    i.setQuantity(i.getQuantity() +
                            item.getQuantity());   //tăng số lượng (nếu nhiều hơn một)
                    return true;
                })
                .orElse(false);
        if (!isExist) { // nếu không tìm thấy
            cartItems.add(item); //thêm item vào danh sách cartItems.
        }
    }
    public void removeItems(Long bookId) {
        cartItems.removeIf(item -> Objects.equals(item.getBookId(),
                bookId));
    }
    public void updateItems(Long bookId, int quantity) {
        cartItems.stream()
                .filter(item -> Objects.equals(item
                        .getBookId(), bookId))
                .forEach(item -> item.setQuantity(quantity));
    }
}


