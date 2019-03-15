package tutorial.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import tutorial.service.QueryService;

@RestController
@RequestMapping("/myapi")
@Api
public class Query {
	
	@Autowired
	QueryService queryService;
	
	@ApiOperation(value = "Get name", notes = "Get yourself a hello")
	@GetMapping(path = "/hello/{name}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure")})
	public String getHello(@PathVariable String name) {
		return queryService.getHello(name);
	}

}
