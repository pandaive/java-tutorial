package tutorial.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "Bill", description = "Data about the bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {

	@ApiModelProperty(dataType = "Integer", value = "Quantity of a product on a bill", required = true, example = "5")
	private int quantity;
	
	@ApiModelProperty(dataType = "Double", value = "Price of a product", required = true, example = "15.99")
	private double price;
	
	@ApiModelProperty(dataType = "Double", value = "VAT rate to be used to calculate the bill", required = false, example = "0.23")
	private double vatRate;
	
}
