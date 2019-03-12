package tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MaximumQuantityExceededException extends RuntimeException {
	
	public MaximumQuantityExceededException(int quantity) {
		super("Quantity " + quantity + " exceeds maximum quantity allowed: 10");
	}

}
