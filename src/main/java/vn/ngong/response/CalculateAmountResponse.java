package vn.ngong.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.payment.AmountProductDto;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class CalculateAmountResponse extends BaseResponse {
	@Schema(name = "productList", description = "Danh sách sản phẩm khác sổ gạo thanh toán", required = true)
	private List<AmountProductDto> productList;

	@Schema(name = "soGaoList", description = "Danh sách sản phẩm sổ gạo thanh toán", required = false)
	private List<AmountProductDto> soGaoList;

	@Schema(name = "originAmount", description = "Tổng tiền ban đầu phải trả", required = true)
	private long originAmount;

	@Schema(name = "amountDiscount", description = "Tổng tiền khuyến mãi", required = true)
	private long amountDiscount;

//	@Schema(name = "shippingFee", description = "Phí ship", required = true)
//	private long shippingFee;

	@Schema(name = "totalAmount", description = "Tổng tiền phải trả (sau khi cộng phí ship và trừ khuyến mãi)", required = true)
	private long totalAmount;
}
