package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.ShippingFee;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class GetShippingFeeResponse extends BaseResponse {
	private ShippingFee shippingFee;
}
