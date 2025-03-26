package site.easy.to.build.crm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.dto.ExpenseByCustomer;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.projection.TicketAndLeadExpenseTotals;
import site.easy.to.build.crm.repository.ExpenseRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

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

    public List<ExpenseByCustomer> getTicketExpensesGroupByCustomer() {
        return expenseRepository.findTicketExpensesGroupByCustomer();
    }

    public List<ExpenseByCustomer> getLeadExpensesGroupByCustomer() {
        return expenseRepository.findLeadExpensesGroupByCustomer();
    }

    public TicketAndLeadExpenseTotals expenseSumsForTicketAndLead() {
        return expenseRepository.expenseSumsForTicketAndLead();
    }
}
