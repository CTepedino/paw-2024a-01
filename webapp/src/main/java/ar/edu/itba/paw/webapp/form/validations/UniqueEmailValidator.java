package ar.edu.itba.paw.webapp.form.validations;

import ar.edu.itba.paw.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserService us;

    @Autowired
    public UniqueEmailValidator(UserService us){
        this.us = us;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return constraintValidatorContext != null && us.findByEmail(s).isEmpty();
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }
}
