package site.easy.to.build.crm.controller.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.DTO.CustomerDuplicationResult;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class CustomerRestController {
    private final PasswordEncoder passwordEncoder;
    private CustomerService customerService;
    private UserService userService;

    @GetMapping("/customer")
    public ResponseEntity<List<Customer>> getCustomer() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/employee")
    public ResponseEntity<List<User>> getEmployee() {
        return ResponseEntity.ok(userService.findAll());
    }


    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/import")
    public ResponseEntity<?> importCustomer(@RequestBody CustomerDuplicationResult duplicata) {
        System.out.println(duplicata);
        // save this in the db
        customerService.save(duplicata);
        return ResponseEntity.ok("Customer imported successfully");
    }
}
