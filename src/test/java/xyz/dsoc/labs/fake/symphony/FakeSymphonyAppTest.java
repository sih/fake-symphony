package xyz.dsoc.labs.fake.symphony;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

/**
 * @author sih
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FakeSymphonyAppTest {

    @Test
    public void testContextLoads() {
        assertTrue(true);
    }

}