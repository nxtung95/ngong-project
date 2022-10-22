package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.PaymentMethod;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class PaymentMethodListResponse extends BaseResponse {
	private List<PaymentMethod> paymentMethodList;
}
