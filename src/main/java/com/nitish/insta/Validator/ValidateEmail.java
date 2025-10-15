package com.nitish.insta.Validator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailDomainValidator.class)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateEmail{
    String message() default "Invalid email";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
}