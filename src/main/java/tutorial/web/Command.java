package tutorial.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tutorial.model.Bill;
import tutorial.service.CommandService;

@RequestMapping("/myapi")
@RestController
public class Command {
	
	@Autowired
	CommandService commandService;
	
	@PostMapping(path = "/price", consumes = "application/json")
	public double calculatePrice(@RequestBody Bill request) {
		return commandService.getPrice(request);
	}
}
