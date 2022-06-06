package planner.config;

import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Log4j2
public class SchedulerConfig {

    @Scheduled(fixedDelay = 5000)
    public void computePrice() {
        log.info("This is sked service every 5 sec " + LocalDateTime.now());
    }
}
