package tutorial.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tutorial.service.QueryService;

@RestController
@RequestMapping("/myapi")
public class Query {
	
	@Autowired
	QueryService queryService;
	
	@GetMapping(path = "/hello/{name}")
	public String getHello(@PathVariable String name) {
		return queryService.getHello(name);
	}

}
