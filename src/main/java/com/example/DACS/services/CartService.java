package com.example.DACS.services;


import com.example.DACS.daos.Cart;
import com.example.DACS.daos.Item;
import com.example.DACS.entity.Invoice;
import com.example.DACS.entity.ItemInvoice;
import com.example.DACS.repository.IBookRepository;
import com.example.DACS.repository.IInvoiceRepository;
import com.example.DACS.repository.IItemInvoiceRepository;
import jakarta.servlet.http.HttpSession;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;



import java.util.Date;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class CartService {
    //hằng số được sử dụng để định danh giỏ hàng trong HttpSession.
    private static final String CART_SESSION_KEY = "cart";
    private final IInvoiceRepository invoiceRepository;
    private final IItemInvoiceRepository itemInvoiceRepository;
    private final IBookRepository bookRepository;

    //lấy giỏ hàng từ session
    public Cart getCart(@NotNull HttpSession session) {
        return Optional.ofNullable((Cart)
                        session.getAttribute(CART_SESSION_KEY)) //lấy giá trị CART_SESSION_KEY từ HttpSession
                .orElseGet(() -> { //nếu không có cart trong session
                    Cart cart = new Cart();
                    session.setAttribute(CART_SESSION_KEY, cart); //lưu giỏ hàng vào session
                    return cart;
                });
    }

    //ghi đè gio hang hien tai trong session vơới giỏ hang dc cung cap
    public void updateCart(@NotNull HttpSession session, Cart cart) {
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    //xóa giỏ hàng khỏi HttpSession bằng cách gỡ bỏ thuộc tính CART_SESSION_KEY khỏi HttpSession.
    public void removeCart(@NotNull HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }


    public int getSumQuantity(@NotNull HttpSession session) {
        return getCart(session).getCartItems().stream() //tạo một luồng của các mục trong giỏ hàng
                .mapToInt(Item::getQuantity) //lấy giá trị số lượng của từng mục
                .sum(); //tính tổng số lượng của từng mỵc
    }
    public double getSumPrice(@NotNull HttpSession session) {
        return getCart(session).getCartItems().stream()
                .mapToDouble(item -> item.getPrice() *
                        item.getQuantity())
                .sum();
    }
    public void saveCart(@NotNull HttpSession session, String buyerName, String buyerAddress, String buyerPhone, String buyerNote,String buyerEmail) {
        var cart = getCart(session); //lấy cart từ session
        if (cart.getCartItems().isEmpty()) return;
        var invoice = new Invoice(); //tạo hóa đơn
        invoice.setInvoiceDate(new Date());
        invoice.setPrice(getSumPrice(session));
        invoice.setBuyerName(buyerName);
        invoice.setBuyerAddress(buyerAddress);
        invoice.setBuyerPhone(buyerPhone);
        invoice.setBuyerNote(buyerNote);
        invoice.setBuyerEmail(buyerEmail);
        invoiceRepository.save(invoice); //lưu hóa đơn
        //phần thêm vào bảng trung gian giữa Invoice và item
        cart.getCartItems().forEach(item -> { //với mỗi mục trong giỏ hàng làm:
            var itemInvoice = new ItemInvoice(); //Tạo một đối tượng ItemInvoice mới để đại diện cho một mục trong hóa đơn.
            itemInvoice.setInvoice(invoice);
            itemInvoice.setQuantity(item.getQuantity());
            itemInvoice.setBook(bookRepository.findById(item.getBookId()).orElseThrow());
            itemInvoiceRepository.save(itemInvoice);
        });
        removeCart(session);
    }

}

