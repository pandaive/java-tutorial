package tutorial.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myapi")
public class Query {
	
	@GetMapping(path = "/hello")
	public String getHello() {
		return "Hello world!";
	}

}
