package site.easy.to.build.crm.repository.settings;

import org.springframework.data.jpa.repository.JpaRepository;
import site.easy.to.build.crm.entity.settings.BudgetSettings;

import java.util.Optional;

public interface BudgetSettingsRepository extends JpaRepository<BudgetSettings, Integer> {

    Optional<BudgetSettings> findFirstByOrderByUpdatedAt();
}
