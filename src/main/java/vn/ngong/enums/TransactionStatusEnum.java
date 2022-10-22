package vn.ngong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum TransactionStatusEnum {
	PAYMENT_NOT_APPROVE(1),
	PAYMENT_APPROVE_NOT_RECEIVE(2),
	FINISH(3),
	NOT_PAYMENT_NOT_APPOVE(4),
	NOT_PAYMENT_APPOVE_NOT_RECEIVE(5),
	CANCEL(6);

	private int label;

	public int label() {
		return label;
	}
}
