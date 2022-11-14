package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.PaymentMethod;
import vn.ngong.entity.TransactionNotify;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class NotifyTransactionListResponse extends BaseResponse {
	private List<TransactionNotify> transactionNotifyList;
}
