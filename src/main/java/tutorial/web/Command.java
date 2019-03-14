package tutorial.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import tutorial.model.Bill;
import tutorial.service.CommandService;

@RequestMapping("/myapi")
@RestController
@Slf4j
public class Command {
	
	@Autowired
	CommandService commandService;
	
	@PostMapping(path = "/price", consumes = "application/json")
	public double calculatePrice(@RequestBody Bill request) {
		log.info("[Command] Received new calculatePrice request. Request body: {}", request);
		return commandService.getPrice(request);
	}
}
