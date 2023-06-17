package junseok.snr.wallet.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Profile({ "deposit-schedule", "withdraw-schedule" })
@EnableScheduling
public class ScheduleConfiguration {
}
