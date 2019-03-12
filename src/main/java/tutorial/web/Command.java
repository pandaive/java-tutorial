package tutorial.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import tutorial.service.CommandService;

@RestController
public class Command {
	
	@Autowired
	CommandService commandService;

}
