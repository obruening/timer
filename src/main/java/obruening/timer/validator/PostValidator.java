package obruening.timer.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import obruening.timer.model.primary.Post;

public class PostValidator implements Validator {

	@Override
    public boolean supports(Class clazz) {
        return Post.class.equals(clazz);
    }

	@Override
	public void validate(Object target, Errors errors) {
		
		Post post = (Post)target;
		ValidationUtils.rejectIfEmpty(errors, "text", "text.empty");
	}

}
