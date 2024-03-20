package com.example.DACS.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @GetMapping
    public String handleError(HttpServletRequest request){
        //trả về trang thái lỗi của yêu cầu, dí dụ: 404, 500
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null){
            int statusCode = Integer.parseInt(status.toString()); //chuyển thành kiểu int để sử dụng status (dạng chuỗi) sang so16 nguyên
            if(statusCode == 404)
                return "error/404";
        }
        return null;
    }
}
