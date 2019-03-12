package tutorial.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tutorial.exception.MaximumQuantityExceededException;
import tutorial.model.Bill;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommandServiceTest {
	
	@Autowired
	CommandService commandService;
	
	@Test
	public void getPriceTest() {
		assertEquals(12.0, commandService.getPrice(new Bill(5, 2, 0.2)), 0);
	}
	
	@Test(expected = MaximumQuantityExceededException.class)
	public void getPriceMaximumQuantityExceededExceptionTest() {
		commandService.getPrice(new Bill(15, 2, 0.2));
	}

}
