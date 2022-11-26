package vn.ngong.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingFeeRequestDto {
	private int cityCode;
	private int districtCode;
	private double weight;
//	private int totalPrice;
}
