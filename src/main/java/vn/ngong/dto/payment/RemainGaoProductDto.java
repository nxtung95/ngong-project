package vn.ngong.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RemainGaoProductDto {
	private String amountFixRemainGao;
	private long remainSizeGao;
	private String amountRemainGao;
}
