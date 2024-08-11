package com.cheong.wcc_assessment.location.validator;

import com.cheong.wcc_assessment.location.repository.OutcodeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class ExistingOutcodeValidator implements ConstraintValidator<ExistingOutcode, String> {

    private final OutcodeRepository outcodeRepository;

    private HttpServletRequest httpServletRequest;

    public ExistingOutcodeValidator(OutcodeRepository outcodeRepository, HttpServletRequest httpServletRequest) {
        this.outcodeRepository = outcodeRepository;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {

        Map<String, String> pathVariablesMap = (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariablesMap.containsKey("outcode")) {
            if (code.equals(pathVariablesMap.get("outcode"))) {
                return true;
            }
        }

        return !outcodeRepository.existsByOutcode(code);
    }
}
