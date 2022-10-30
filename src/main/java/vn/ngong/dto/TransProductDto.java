package vn.ngong.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.kiotviet.obj.Attribute;

@Getter
@Setter
@Builder(toBuilder = true)
public class TransProductDto {
	@Schema(name = "productCode", description = "Mã sản phẩm")
	private String productCode;
	@Schema(name = "quantity", description = "Số lượng sản phẩm")
	private int quantity;
	@Schema(name = "attribute", description = "Thuộc tính của 1 sản phẩm, VD: kg, ml")
	private Attribute attribute;
	@Schema(name = "price", description = "Giá 1 sản phẩm")
	private int price;
	@Schema(name = "priceDiscount", description = "Giá 1 sản phẩm sau khuyến mại")
	private int priceDiscount;
	@Schema(name = "gaoFlag", description = "0:sản phẩm không phải gạo, 1: sản phẩm gạo")
	private int gaoFlag;
}
