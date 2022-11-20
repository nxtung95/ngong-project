package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.payment.RemainGaoProductDto;
import vn.ngong.dto.payment.ResponseTransProductDto;
import vn.ngong.entity.Transaction;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class PaymentResponse extends BaseResponse {
	private Transaction transaction;
	private List<ResponseTransProductDto> productList;
	private RemainGaoProductDto remainGaoProduct;
}
