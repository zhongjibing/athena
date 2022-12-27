package com.icezhg.athena.annotation.validate;

import com.icezhg.athena.annotation.validate.constraint.GrantTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by zhongjibing on 2022/12/25.
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = GrantTypeValidator.class)
public @interface GrantType {
    String message() default "{jakarta.validation.constraints.Oauth2GrantType.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String delimiter() default ",";
}
