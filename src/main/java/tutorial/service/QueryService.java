package tutorial.service;

import org.springframework.stereotype.Component;

@Component
public class QueryService {
	
	public String getHello(String name) {
		return "Hello " + name + "!";
	}
	
}
