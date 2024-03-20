package com.example.DACS.controller;

import com.example.DACS.daos.Cart;
import com.example.DACS.daos.Item;
import com.example.DACS.entity.Book;
import com.example.DACS.entity.ItemInvoice;
import com.example.DACS.services.CartService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.List;


@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping
    public String showCart(HttpSession session,
                           @NotNull Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("totalPrice",
                cartService.getSumPrice(session));
        model.addAttribute("totalQuantity",
                cartService.getSumQuantity(session));
        return "book/cart";
    }
    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(HttpSession session,
                                 @PathVariable Long id) {
        var cart = cartService.getCart(session);
        cart.removeItems(id);
        return "redirect:/cart";
    }
    @GetMapping("/updateCart/{id}/{quantity}")
    public String updateCart(HttpSession session, @PathVariable Long id, @PathVariable int quantity) {
        var cart = cartService.getCart(session);
        cart.updateItems(id, quantity);
        return "book/cart";
    }
    @GetMapping("/clearCart")
    public String clearCart(HttpSession session) {
        cartService.removeCart(session);
        return "redirect:/cart ";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session,
                           @RequestParam("buyerName") String buyerName,
                           @RequestParam("buyerAddress") String buyerAddress,
                           @RequestParam("buyerPhone") String buyerPhone,
                           @RequestParam("buyerNote") String buyerNote,
                           @RequestParam("buyerEmail") String buyerEmail) {

        try {
            //Đối tượng này được sử dụng để tạo một email dạng MIME
            // (Multipurpose Internet Mail Extensions),
            // có thể chứa nhiều phương thức và tài liệu đa phương tiện.
            MimeMessage message = mailSender.createMimeMessage();

            //Đối tượng này được sử dụng để thiết lập các thuộc tính của email, như địa chỉ gửi, địa chỉ nhận,
            // chủ đề, nội dung
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(buyerEmail); //người nhận
            helper.setSubject("HHT BookShop"); //chủ đề của mail

            StringBuilder htmlContent = new StringBuilder(); //StringBuilder được sử dụng để tạo nội dung HTML của email.
            htmlContent.append("<h1>Cảm ơn bạn đã mua hàng ở HHT BookShop <3</h1>");
            htmlContent.append("<br><br>");
            htmlContent.append("<h2>Thông tin đơn hàng:</h2>");
            htmlContent.append("<table style='border-collapse: collapse;'>");
            htmlContent.append("<thead><tr><th style='border: 1px solid black;'>Mã sách</th><th style='border: 1px solid black;'>Tên sách</th><th style='border: 1px solid black;'>Giá</th><th style='border: 1px solid black;'>Số lượng</th></tr></thead>");
            htmlContent.append("<tbody>");

            Cart cart = cartService.getCart(session); //lấy giỏ hàng từ session
            List<Item> cartItems = cart.getCartItems(); //lấy các items từ giỏ hàng
            for (Item item : cartItems) { //với mỗi item
                htmlContent.append("<tr>");
                htmlContent.append("<td style='border: 1px solid black;'>").append(item.getBookId()).append("</td>");
                htmlContent.append("<td style='border: 1px solid black;'>").append(item.getBookName()).append("</td>");
                htmlContent.append("<td style='border: 1px solid black;'>").append(item.getPrice()).append("</td>");
                htmlContent.append("<td style='border: 1px solid black;'>").append(item.getQuantity()).append("</td>");
                htmlContent.append("</tr>");
            }

            htmlContent.append("</tbody>");
            htmlContent.append("<tfoot>");
            htmlContent.append("<tr>");
            htmlContent.append("<td colspan='3' style='text-align: right; border: 1px solid black;'>Tổng giá trị:</td>");
            htmlContent.append("<td style='border: 1px solid black;'>").append(cartService.getSumPrice(session)).append("</td>");
            htmlContent.append("</tr>");
            htmlContent.append("</tfoot>");
            htmlContent.append("</table>");

            helper.setText(htmlContent.toString(), true); //đặt nội dung của email là mã html

            mailSender.send(message); //gửi mail
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            // Xử lý lỗi gửi email (nếu có)
            e.printStackTrace();
        }

        cartService.saveCart(session, buyerName, buyerAddress, buyerPhone, buyerNote, buyerEmail);

        return "redirect:/cart";
    }







}
