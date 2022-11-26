package vn.ngong.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.dto.payment.ShippingFeeRequestDto;
import vn.ngong.dto.payment.TransCustomerDto;
import vn.ngong.dto.payment.TransProductDto;
import vn.ngong.dto.payment.TransSoGaoDto;

import java.util.List;

@Getter
@Setter
public class CalculateAmountRequest {
	@Schema(name = "customer", description = "Thông tin user nhận hàng", required = true)
	private TransCustomerDto customer;

	@Schema(name = "productList", description = "Danh sách sản phẩm khác sổ gạo thanh toán", required = true)
	private List<TransProductDto> productList;

	@Schema(name = "soGaoList", description = "Danh sách sản phẩm sổ gạo thanh toán", required = false)
	private List<TransSoGaoDto> soGaoList;

	@Schema(name = "shippingFeeInfo", description = "Thông tin tính phí ship", required = false)
	private ShippingFeeRequestDto shippingFeeInfo;

	@Schema(name = "isCalculateAgain", description = "Tính lại phí trong trường hợp sổ gạo trong sổ không đủ để thanh toán sản phẩm gạo", required = false)
	private int isCalculateAgain;
}
