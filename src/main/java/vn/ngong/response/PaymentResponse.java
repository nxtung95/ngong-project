package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.ResponseTransProductDto;
import vn.ngong.entity.Transaction;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class PaymentResponse extends BaseResponse {
	private Transaction transaction;
	private List<ResponseTransProductDto> productList;
}
