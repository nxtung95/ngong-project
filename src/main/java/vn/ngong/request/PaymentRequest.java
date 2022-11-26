package vn.ngong.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.dto.payment.RemainGaoProductDto;
import vn.ngong.dto.payment.TransCustomerDto;
import vn.ngong.dto.payment.TransProductDto;
import vn.ngong.dto.payment.TransSoGaoDto;
import vn.ngong.entity.User;

import java.util.List;

@Getter
@Setter
public class PaymentRequest {
	@Schema(name = "customer", description = "Thông tin user nhận hàng", required = true)
	private TransCustomerDto customer;

	@Schema(name = "remainGaoProductList", description = "Danh sách số gạo còn thừa được quy đổi sang tiền khi thanh toán sổ gạo", required = false)
	private List<RemainGaoProductDto> remainGaoProductList;

	@Schema(name = "productList", description = "Danh sách sản phẩm khác sổ gạo thanh toán", required = true)
	private List<TransProductDto> productList;

	@Schema(name = "soGaoList", description = "Danh sách sản phẩm sổ gạo thanh toán", required = false)
	private List<TransSoGaoDto> soGaoList;

	@Schema(name = "promotionCode", description = "Mã giảm giá")
	private String promotionCode;

	@Schema(name = "paymentMethodId", description = "Phương thức thanh toán", required = true)
	private String paymentMethodId;

	@Schema(name = "originAmount", description = "Tổng tiền ban đầu phải trả", required = true)
	private long originAmount;

	@Schema(name = "amountDiscount", description = "Tổng tiền sau khi trừ khuyến mãi")
	private long amountDiscount;

	@Schema(name = "shippingFee", description = "Phí ship")
	private int shippingFee;

	@Schema(name = "totalAmount", description = "Tổng tiền phải trả (sau khi cộng phí ship)", required = true)
	private long totalAmount;
}
