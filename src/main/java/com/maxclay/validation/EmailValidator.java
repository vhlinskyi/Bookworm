package com.maxclay.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
	
	private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


	@Override
	public void initialize(ValidEmail constraintAnnotation) {
		
	}
	
	/*
	 * For error message identification. Target of this validator - validate not null and not empty email.
	 * @NotNull and @NotEmpty are responsible for checking the above problems.
	 * Hibernate @Email validator does the same with empty and null String? 
	 */
	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		
		if(username == null || username.equals(""))
			return true;
		
		return (validate(username));
	}

	private boolean validate(String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
	}

}
