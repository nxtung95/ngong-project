package vn.ngong.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RemainGaoProductDto {
	private long amountFixRemainGao;
	private long remainSizeGao;
	private long amountRemainGao;
}
