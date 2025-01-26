package ar.edu.itba.paw.webapp.form.validations;

import ar.edu.itba.paw.interfaces.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistingEmailValidator implements ConstraintValidator<ExistingEmail, String> {
    private final UserService us;

    @Autowired
    public ExistingEmailValidator(UserService us){
        this.us = us;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return us.findByEmail(s).isPresent();
    }

    @Override
    public void initialize(ExistingEmail constraintAnnotation) {}
}
