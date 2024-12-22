package ar.edu.itba.paw.webapp.dto.form.validations;

import ar.edu.itba.paw.interfaces.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoggedUserPasswordValidator implements ConstraintValidator<LoggedUserPassword, String> {

    private final UserService us;

    @Autowired
    public LoggedUserPasswordValidator(UserService us){
        this.us = us;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return us.isCurrentUserPassword(s);
    }

    @Override
    public void initialize(LoggedUserPassword constraintAnnotation) {}
}
