package vn.ngong.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseTransProductDto {
	private String productCode;
	@Schema(name = "totalProductStock", description = "Tổng số lượng sản phẩm còn lại trong kho")
	private int totalProductStock;
	@Schema(name = "totalCurrentProduct", description = "Số lượng sản phẩm mua hiện tại")
	private int totalCurrentProduct;
	@Schema(name = "totalMinimumProduct", description = "Số lượng sản phẩm tối thiểu cần update")
	private int totalMinimumProduct;
}
