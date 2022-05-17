package planner;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import planner.config.TestConfig;

@ContextConfiguration(classes = {TestConfig.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public abstract class AbstractTest {

}
