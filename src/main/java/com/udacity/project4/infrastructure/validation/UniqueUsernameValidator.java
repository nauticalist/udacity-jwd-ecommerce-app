package com.udacity.project4.infrastructure.validation;

import com.udacity.project4.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<IUniqueUsername, String> {
    private final UserRepository userRepository;

    @Autowired
    public UniqueUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void initialize(IUniqueUsername constraint) {
    }

    public boolean isValid(String username, ConstraintValidatorContext context) {
        return username != null && userRepository.findByUsername(username).isEmpty();
    }
}
