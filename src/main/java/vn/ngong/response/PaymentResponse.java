package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.Transaction;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class PaymentResponse extends BaseResponse {
	private Transaction transaction;
}
