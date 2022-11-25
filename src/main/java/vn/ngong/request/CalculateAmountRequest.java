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

	@Schema(name = "shippingFeeInfo", description = "Thông tin đơn hàng để tính phí ship", required = true)
	private ShippingFeeRequestDto shippingFeeInfo;
}
