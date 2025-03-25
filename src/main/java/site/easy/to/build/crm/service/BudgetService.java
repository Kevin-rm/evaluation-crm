package site.easy.to.build.crm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.dto.BudgetDTO;
import site.easy.to.build.crm.dto.CustomerBudgetSummaryDTO;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Parameter;
import site.easy.to.build.crm.repository.BudgetRepository;
import site.easy.to.build.crm.service.budget.ParameterService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.settings.BudgetSettingsService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static site.easy.to.build.crm.dto.CustomerBudgetSummaryDTO.BudgetStatusAlert.*;

@RequiredArgsConstructor
@Service
public class BudgetService {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100.00");

    private final BudgetRepository budgetRepository;
    private final BudgetSettingsService budgetSettingsService;
    private final ParameterService parameterService;
    private final CustomerService customerService;

    public Budget findById(Integer id) {
        return budgetRepository.findById(id).orElse(null);
    }

    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }

    public List<Budget> getByCustomer(Integer customerId) {
        return budgetRepository.findByCustomerCustomerId(customerId);
    }

    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }

    public void delete(Budget budget) {
        budgetRepository.delete(budget);
    }

    public BudgetDTO getBudgetGlobal(Integer customerId){
        BudgetDTO budgetDTO = new BudgetDTO();
        Object[] objets = (Object[]) budgetRepository.getBudgetsAfterExpenseRawGlobal(customerId);
        budgetDTO.setBudgetId(customerId);
        if (objets == null){
            budgetDTO.setInitialAmount(0.0);
            budgetDTO.setCurrentAmount(0.0);
        }else{
            budgetDTO.setInitialAmount(objets[0] != null ? ((BigDecimal) objets[0]).doubleValue() : 0.0);
            budgetDTO.setCurrentAmount(objets[1] != null ? ((BigDecimal) objets[1]).doubleValue() : 0.0);
        }

        return budgetDTO;
    }

    public BudgetDTO getBudgetDTOGlobal(Integer customerId) {
        Parameter parameter = parameterService.findThresholdAlert();
        BudgetDTO budgetDTO = getBudgetGlobal(customerId);
        double threshold = budgetDTO.getInitialAmount() * parameter.getParameterValue() / 100;
        if (budgetDTO.getInitialAmount() - budgetDTO.getCurrentAmount() >= threshold) {
            budgetDTO.setStatus("Alerte budget au plafon " + parameter.getParameterValue() + " % \n " +
                    "Budget initial : " + budgetDTO.getInitialAmount() + "\n" +
                    "Budget Actuel :  " + budgetDTO.getCurrentAmount() + "\n");
        } else {
            budgetDTO.setStatus("Budget normal");
        }
        return budgetDTO;
    }

    public CustomerBudgetSummaryDTO getCustomerBudgetSummaryDTO(Integer customerId) {
        Object[] rawCustomerBudgetSummary = (Object[]) budgetRepository.getCustomerBudgetSummary(customerId);

        BigDecimal initialAmount = (BigDecimal) rawCustomerBudgetSummary[1];
        BigDecimal currentAmount = (BigDecimal) rawCustomerBudgetSummary[2];

        BigDecimal alertThreshold = budgetSettingsService.getDefault().getAlertThreshold();
        BigDecimal thresholdValue = initialAmount.multiply(alertThreshold).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);

        CustomerBudgetSummaryDTO result = new CustomerBudgetSummaryDTO(
            (Integer) rawCustomerBudgetSummary[0], initialAmount, currentAmount, alertThreshold);
        result.setStatus((result.totalExpenses().compareTo(thresholdValue) >= 0) ? WARNING : OK);

        return result;
    }

    // API SERVICE

    public List<BudgetDTO> getBudgetDTOSGlobalGrouped() {
        List<Customer> customers = customerService.findAll();
        List<BudgetDTO> budgetDTOS = new ArrayList<>();
        for (Customer customer : customers) {
            budgetDTOS.add(getBudgetDTOGlobal(customer.getCustomerId()));
        }
        return budgetDTOS;
    }

    public Map<Integer, Double> getBudgetsByCustomer() {
        List<Budget> budgets = budgetRepository.findAll();
        return budgets.stream()
                .collect(Collectors.groupingBy(
                        budget -> budget.getCustomer().getCustomerId(),
                        Collectors.summingDouble(Budget::getAmount) // Additionner les montants
                ));
    }

    public BigDecimal getTotalBudget() {
        List<Budget> budgets = budgetRepository.findAll();
        return budgets.stream()
                .map(budget -> BigDecimal.valueOf(budget.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
