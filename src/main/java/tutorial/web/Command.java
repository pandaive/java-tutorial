package tutorial.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import tutorial.model.Bill;
import tutorial.service.CommandService;

@Api
@RequestMapping("/myapi")
@RestController
@Slf4j
public class Command {
	
	@Autowired
	CommandService commandService;
	
	@ApiOperation(value = "Calculate price", notes = "Send your bill, get the price")
	@PostMapping(path = "/price", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Double.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure")})
	public double calculatePrice(@RequestBody Bill request) {
		log.info("[Command] Received new calculatePrice request. Request body: {}", request);
		return commandService.getPrice(request);
	}
}
