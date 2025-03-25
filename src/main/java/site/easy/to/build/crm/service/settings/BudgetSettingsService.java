package site.easy.to.build.crm.service.settings;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.settings.BudgetSettings;
import site.easy.to.build.crm.repository.settings.BudgetSettingsRepository;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class BudgetSettingsService {

    private final BudgetSettingsRepository budgetSettingsRepository;

    public BudgetSettings getDefault() {
        return budgetSettingsRepository.findFirstByOrderByUpdatedAt()
            .orElseGet(() -> budgetSettingsRepository.save(BudgetSettings.createDefault()));
    }

    public BudgetSettings updateDefaultAlertThreshold(BigDecimal newAlertThreshold) {
        BudgetSettings settings = getDefault();
        settings.setAlertThreshold(newAlertThreshold);

        return budgetSettingsRepository.save(settings);
    }
}
