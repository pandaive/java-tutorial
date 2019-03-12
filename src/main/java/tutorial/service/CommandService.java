package tutorial.service;

import org.springframework.stereotype.Component;

import tutorial.exception.MaximumQuantityExceededException;
import tutorial.model.Bill;

@Component
public class CommandService {
	
	public double getPrice(Bill bill) {
		if (bill.getQuantity() > 10) {
			throw new MaximumQuantityExceededException(bill.getQuantity());
		}
		return (bill.getPrice() + bill.getPrice() * bill.getVatRate()) * bill.getQuantity();
	}

}
