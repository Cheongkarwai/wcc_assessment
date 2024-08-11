package com.cheong.wcc_assessment.authentication.validator;

import com.cheong.wcc_assessment.authentication.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class ExistingUsernameValidator implements ConstraintValidator<ExistingUsername, String> {

    private final UserRepository userRepository;

    private final HttpServletRequest httpServletRequest;

    public ExistingUsernameValidator(UserRepository userRepository,
                                     HttpServletRequest httpServletRequest){
        this.userRepository = userRepository;
        this.httpServletRequest = httpServletRequest;
    }
    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        Map<String, String> pathVariablesMap = (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariablesMap.containsKey("username")) {
            if (username.equals(pathVariablesMap.get("username"))) {
                return true;
            }
        }

        return !userRepository.existsById(username);
    }
}
