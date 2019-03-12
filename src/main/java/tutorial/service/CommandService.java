package tutorial.service;

import org.springframework.stereotype.Component;

import tutorial.model.Bill;

@Component
public class CommandService {
	
	public double getPrice(Bill bill) {
		return (bill.getPrice() + bill.getPrice() * bill.getVatRate()) * bill.getQuantity();
	}

}
