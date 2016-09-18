package me.loki2302;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = App.class, properties = "spring.profiles.active=default")
@RunWith(SpringRunner.class)
@Transactional(rollbackFor = Throwable.class)
public abstract class AbstractServiceTest {    
}
