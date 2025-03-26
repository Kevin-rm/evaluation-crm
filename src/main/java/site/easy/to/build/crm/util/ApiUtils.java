package site.easy.to.build.crm.util;

import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public abstract class ApiUtils {

    public static Map<String, String> validationErrorsToMap(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(
            fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()
        ));

        return errors;
    }
}
