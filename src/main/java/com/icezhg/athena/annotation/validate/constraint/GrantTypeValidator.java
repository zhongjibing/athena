package com.icezhg.athena.annotation.validate.constraint;

import com.icezhg.athena.annotation.validate.GrantType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.List;
import java.util.Set;

/**
 * Created by zhongjibing on 2022/12/25.
 */
public class GrantTypeValidator implements ConstraintValidator<GrantType, String> {

    private static final Set<String> EXPECTS = Set.of(AuthorizationGrantType.AUTHORIZATION_CODE.getValue(),
            AuthorizationGrantType.CLIENT_CREDENTIALS.getValue(), AuthorizationGrantType.REFRESH_TOKEN.getValue());
    private String delimiter;

    @Override
    public void initialize(GrantType grantType) {
        this.delimiter = grantType.delimiter();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        List<String> checkingValues = delimiter.isEmpty() ? List.of(value) : List.of(value.split(delimiter));
        return EXPECTS.containsAll(checkingValues);
    }
}
