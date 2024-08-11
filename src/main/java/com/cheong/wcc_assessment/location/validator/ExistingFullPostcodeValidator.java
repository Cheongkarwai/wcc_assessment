package com.cheong.wcc_assessment.location.validator;

import com.cheong.wcc_assessment.location.repository.FullPostcodeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class ExistingFullPostcodeValidator implements ConstraintValidator<ExistingFullPostcode, String> {

    private final FullPostcodeRepository fullPostcodeRepository;

    private final HttpServletRequest httpServletRequest;

    public ExistingFullPostcodeValidator(FullPostcodeRepository fullPostcodeRepository,
                                         HttpServletRequest httpServletRequest){
        this.fullPostcodeRepository = fullPostcodeRepository;
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

        return !fullPostcodeRepository.existsByPostcode(code);
    }
}
