package site.easy.to.build.crm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.repository.ExpenseRepository;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final TicketService ticketService;
    private final LeadService leadService;
    private final CustomerService customerService;

    public List<Expense> getAll() {
        return expenseRepository.findAll();
    }

    public Optional<Expense> getById(Integer id) {
        return expenseRepository.findById(id);
    }

    public void save(Expense expense) {
        expenseRepository.save(expense);
    }

    public void delete(Expense expense) {
        expenseRepository.delete(expense);
    }

    ///  API
    public Map<Integer,Double> getTicketExpensesByCustomer(){
        List<Customer> customers = customerService.findAll();
        Map<Integer,Double> customExpense = new HashMap<>();
        for(Customer customer : customers){
            List<Ticket> ticketsCust = ticketService.findCustomerTickets(customer.getCustomerId());
            Double expense = 0.0;
            for (Ticket ticket : ticketsCust) {
                expense += ticket.getExpense().getAmount();
            }
            customExpense.put(customer.getCustomerId(),expense);
        }
        return customExpense;
    }

    public Map<Integer,Double> getLeadExpensesByCustomer(){
        List<Customer> customers = customerService.findAll();
        Map<Integer,Double> customExpense = new HashMap<>();
        for(Customer customer : customers){
            List<Lead> leadsCust = leadService.getLeadsByCustomerId(customer.getCustomerId());
            Double expense = 0.0;
            for (Lead ticket : leadsCust) {
                expense += ticket.getExpense().getAmount();
            }
            customExpense.put(customer.getCustomerId(),expense);
        }
        return customExpense;
    }

    public BigDecimal getTotalTicketExpenses() {
        List<Expense> expenses = expenseRepository.findAllByTicketIsNotNull();
        return expenses.stream()
                .map(expense -> BigDecimal.valueOf(expense.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalLeadExpenses() {
        List<Expense> expenses = expenseRepository.findAllByLeadIsNotNull();
        return expenses.stream()
                .map(expense -> BigDecimal.valueOf(expense.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
