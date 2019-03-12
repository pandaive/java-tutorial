package tutorial.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tutorial.service.QueryService;

@RestController
@RequestMapping("/myapi")
public class Query {
	
	@Autowired
	QueryService queryService;
	
	@GetMapping(path = "/hello")
	public String getHello() {
		return queryService.getHello();
	}

}
