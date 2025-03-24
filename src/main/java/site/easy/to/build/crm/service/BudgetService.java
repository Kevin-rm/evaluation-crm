package site.easy.to.build.crm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.DTO.BudgetDTO;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Parameter;
import site.easy.to.build.crm.repository.BudgetRepository;
import site.easy.to.build.crm.service.budget.ParameterService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.settings.BudgetSettingsService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BudgetService {
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

    public List<Budget> findBudgetsByCustomerId(int customerId) {
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        return budgetRepository.findBudgetByCustomer(customer);
    }

    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }

    public Budget update(Budget budget) {
        return budgetRepository.save(budget);
    }

    public void delete(Budget budget) {
        budgetRepository.delete(budget);
    }

    public List<BudgetDTO> setStatus(List<BudgetDTO> budgets) {
        Parameter parameter = parameterService.findThresholdAlert();
        for (BudgetDTO budget : budgets) {
            double threshold = budget.getInitialAmount() * parameter.getParameterValue() / 100;
            if (budget.getInitialAmount() - budget.getCurrentAmount() >= threshold) {
                budget.setStatus("Alerte budget a " + parameter.getParameterValue() + " % "
                        + "Budget initial : " + budget.getInitialAmount() + "\n" +
                        "Budget Actuel :  " + budget.getCurrentAmount() + "\n");
            } else {
                budget.setStatus("Budget normal");
            }
        }
        return budgets;
    }

//    public Double cumulBudget(Integer customerId) {
//        return ((BigDecimal) budgetRepository.getBudgetsAfterExpenseRawGlobal(customerId)).doubleValue();
//    }

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
