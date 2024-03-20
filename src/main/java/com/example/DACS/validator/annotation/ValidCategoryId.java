package com.example.DACS.validator.annotation;

import com.example.DACS.validator.ValidCategoryIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

//@Target({ElementType.TYPE, ElementType.FIELD}): Xác định rằng annotation có thể được áp dụng cho các thành phần kiểu TYPE (lớp, interface, enum) và FIELD (trường).
//@Retention(RetentionPolicy.RUNTIME): Xác định rằng annotation sẽ được giữ lại và sử dụng trong thời gian chạy.
//@Constraint(validatedBy = ValidCategoryIdValidator.class): Xác định lớp validator ValidCategoryIdValidator sẽ được sử dụng để kiểm tra tính hợp lệ của trường được gắn kết với annotation này.
//@Documented: Xác định rằng annotation này sẽ được tài liệu hóa trong các tài liệu API.
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidCategoryIdValidator.class)
@Documented
public @interface ValidCategoryId {
    String message() default "Invalid Category ID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
