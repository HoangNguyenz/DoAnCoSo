package com.example.DACS.validator;

import com.example.DACS.repository.IUserRepository;
import com.example.DACS.validator.annotation.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidUsernameValidator  implements ConstraintValidator<ValidUsername, String> {
    @Autowired
    private IUserRepository userRepository;

//    IUserRepository để kiểm tra xem đã tồn tại một người dùng với username đã cho hay chưa.
//    Nếu không có người dùng nào có cùng username, validator trả về true, ngược lại trả về false.
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context){
        if (userRepository == null)
            return true;
        return userRepository.findByUsername(username) == null;
    }

}
