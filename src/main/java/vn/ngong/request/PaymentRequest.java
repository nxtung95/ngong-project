package vn.ngong.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.dto.RequestTransCustomerDto;
import vn.ngong.dto.RequestTransProductDto;

import java.util.List;

@Getter
@Setter
public class PaymentRequest {
	@Schema(name = "Customer", description = "Thông tin user nhận hàng", required = true)
	private RequestTransCustomerDto customer;

	@Schema(name = "productList", description = "Danh sách product thanh toán", required = true)
	private List<RequestTransProductDto> productList;

	@Schema(name = "promotionCode", description = "Mã giảm giá")
	private String promotionCode;

	@Schema(name = "paymentMethodId", description = "Phương thức thanh toán", required = true)
	private String paymentMethodId;

	@Schema(name = "originAmount", description = "Tổng tiền ban đầu phải trả", required = true)
	private String originAmount;

	@Schema(name = "amountDiscount", description = "Tổng tiền khuyến mãi")
	private String amountDiscount;

	@Schema(name = "shippingFee", description = "Phí ship")
	private String shippingFee;

	@Schema(name = "totalAmount", description = "Tổng tiền phải trả (sau khi cộng phí ship và trừ khuyến mãi)", required = true)
	private String totalAmount;
}
