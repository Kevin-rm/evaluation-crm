package site.easy.to.build.crm.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.request.LoginRequest;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.ApiUtils;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class SecurityController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @Valid @RequestBody LoginRequest request,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(
                Collections.singletonMap("errors", ApiUtils.validationErrorsToMap(bindingResult)
            ));

        User user = userService.findByEmail(request.getEmail());
        return user == null ? ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Collections.singletonMap("error", "Identifiant invalide")) : ResponseEntity.ok(user);
    }
}
