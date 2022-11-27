package vn.ngong.dto.payment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemainGaoProductDto {
	private int productId;
	private String productCode;
	private long amountFix;
	private long quantity;
	private long totalAmount;
}
