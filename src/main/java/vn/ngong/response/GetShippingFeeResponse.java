package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class GetShippingFeeResponse extends BaseResponse {
	private ShippingFee shippingFee;
}
