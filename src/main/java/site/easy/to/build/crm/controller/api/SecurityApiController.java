package site.easy.to.build.crm.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.dto.UserDTO;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.request.LoginRequest;
import site.easy.to.build.crm.response.ApiResponse;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.ApiUtils;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class SecurityApiController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<? extends ApiResponse<?>> login(
        @Valid @RequestBody LoginRequest request,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) return ApiResponse.error(
            "Validation failed", ApiUtils.validationErrorsToMap(bindingResult)).toResponseEntity();

        User user = userService.findByEmail(request.getEmail());
        return user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword()) ?
            ApiResponse.error(HttpStatus.UNAUTHORIZED, "Invalid credentials").toResponseEntity() :
            ApiResponse.success("Login successful", UserDTO.createFromUser(user)).toResponseEntity();
    }
}
