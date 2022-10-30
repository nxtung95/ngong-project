package vn.ngong.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.kiotviet.obj.Attribute;

@Getter
@Setter
@Builder(toBuilder = true)
public class TransSoGaoDto {
	@Schema(name = "productCode", description = "Mã sổ gạo")
	private String productCode;
	@Schema(name = "quantity", description = "Số lượng")
	private int quantity;
	@Schema(name = "attribute", description = "Size của sổ gạo, value là số kg")
	private Attribute attribute;
	@Schema(name = "price", description = "Giá 1 sản phẩm")
	private int price;
	@Schema(name = "priceDiscount", description = "Giá 1 sản phẩm sau khuyến mại")
	private int priceDiscount;
}
