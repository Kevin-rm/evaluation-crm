package site.easy.to.build.crm.customValidations.budget;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import site.easy.to.build.crm.entity.Budget;

public class StartDateBeforeEndDateValidator implements ConstraintValidator<StartDateBeforeEndDate, Budget> {

    @Override
    public void initialize(StartDateBeforeEndDate constraintAnnotation) { }

    @Override
    public boolean isValid(Budget budget, ConstraintValidatorContext context) {
        if (budget == null || budget.getStartDate() == null || budget.getEndDate() == null) return true;

        return budget.getStartDate().isBefore(budget.getEndDate());
    }
}
