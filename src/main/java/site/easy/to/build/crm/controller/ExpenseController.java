package site.easy.to.build.crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.DTO.BudgetDTO;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.BudgetService;
import site.easy.to.build.crm.service.ExpenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/employee/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final BudgetService budgetService;
    private final LeadService leadService;
    private final TicketService ticketService;

    @GetMapping("/form")
    public String showCreateForm(
        @RequestParam(required = false) Integer leadId,
        @RequestParam(required = false) Integer ticketId,
        Model model
    ) {
        final boolean modelContainsExpenseAttribute = model.containsAttribute("expense");
        final BudgetDTO budgetDTOGlobal;
        Expense expense = null;
        if (leadId != null) {
            Lead lead = leadService.findByLeadId(leadId);
            if (lead == null) return "error/not-found";

            if (lead.getExpense() != null && !modelContainsExpenseAttribute) expense = lead.getExpense();
            budgetDTOGlobal = budgetService.getBudgetDTOGlobal(lead.getCustomer().getCustomerId());

            model.addAttribute("leadId", leadId);
        } else if (ticketId != null) {
            Ticket ticket = ticketService.findByTicketId(ticketId);
            if (ticket == null) return "error/not-found";

            if (ticket.getExpense() != null && !modelContainsExpenseAttribute) expense = ticket.getExpense();
            budgetDTOGlobal = budgetService.getBudgetDTOGlobal(ticket.getCustomer().getCustomerId());

            model.addAttribute("ticketId", ticketId);
        } else return "error/400";

        model.addAttribute("budgetDTOGlobal", budgetDTOGlobal);
        if (!modelContainsExpenseAttribute)
            model.addAttribute("expense", expense == null ? new Expense() : expense);

        return "expense/create-expense";
    }

    @PostMapping("/form")
    public String createExpense(
        @Valid @ModelAttribute Expense expense,
        @RequestParam(required = false) Integer leadId,
        @RequestParam(required = false) Integer ticketId,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.expense", bindingResult);
            redirectAttributes.addFlashAttribute("expense", expense);

            return "redirect:/employee/expense/form%s".formatted(leadId != null ? "?leadId=" + leadId : "?ticketId=" + ticketId);
        }

        if (leadId != null) {
            Lead lead = leadService.findByLeadId(leadId);
            if (lead == null) return "error/not-found";

            expense.setCustomer(lead.getCustomer());
            expenseService.save(expense);
            lead.setExpense(expense);
            leadService.save(lead);

            return "redirect:/employee/lead/show/" + leadId;
        } else if (ticketId != null) {
            Ticket ticket = ticketService.findByTicketId(ticketId);
            if (ticket == null) return "error/not-found";

            expense.setCustomer(ticket.getCustomer());
            expenseService.save(expense);
            ticket.setExpense(expense);
            ticketService.save(ticket);

            return "redirect:/employee/ticket/show-ticket/" + ticketId;
        }

        return "error/400";
    }
}
