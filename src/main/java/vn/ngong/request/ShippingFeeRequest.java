package vn.ngong.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingFeeRequest {
	private String cityCode;
	private String districtCode;
	private int totalAmount;
}
