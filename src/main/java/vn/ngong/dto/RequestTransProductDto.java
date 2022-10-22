package vn.ngong.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTransProductDto {
	private String productCode;
	private int quantity;
	@Schema(name = "price", description = "Giá 1 sản phẩm")
	private int price;
	@Schema(name = "priceDiscount", description = "Giá 1 sản phẩm sau khuyến mại")
	private int priceDiscount;
}
