package site.easy.to.build.crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.service.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/employee/budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final CustomerService customerService;

    @GetMapping("/create/{customerId}")
    public String showCreateBudgetForm(@PathVariable Integer customerId, Model model) {
        Customer customer = customerService.findByCustomerId(customerId);
        if (customer == null) return "error/not-found";

        if (!model.containsAttribute("budget")) {
            Budget budget = new Budget();
            budget.setCustomer(customer);

            model.addAttribute("budget", budget);
        }

        return "budget/create-budget";
    }

    @PostMapping("/create/{customerId}")
    public String createBudget(
        @PathVariable Integer customerId,
        @Valid @ModelAttribute Budget budget,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        Customer customer = customerService.findByCustomerId(customerId);
        if (customer == null) return "error/not-found";

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.budget", bindingResult);
            redirectAttributes.addFlashAttribute("budget", budget);

            return "redirect:/employee/budget/create/" + customerId;
        }

        try {
            budget.setCustomer(customer);
            budgetService.save(budget);

            return "redirect:/employee/customer/" + customerId;
        } catch (Exception e) {
            return "error/500";
        }
    }

    @GetMapping("/customer/{customerId}")
    public String budgetsByCustomer(@PathVariable Integer customerId, Model model) {
        Customer customer = customerService.findByCustomerId(customerId);
        if (customer == null) return "error/not-found";

        model.addAttribute("budgets", budgetService.getByCustomer(customerId));
        model.addAttribute("customer", customer);

        return "budget/customer-budgets";
    }
}
