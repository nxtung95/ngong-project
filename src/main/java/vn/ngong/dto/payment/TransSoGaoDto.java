package vn.ngong.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import vn.ngong.kiotviet.obj.Attribute;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransSoGaoDto {
	@Schema(name = "productId", description = "ID sản phẩm")
	private int productId;
	@Schema(name = "productCode", description = "Mã sổ gạo")
	private String productCode;
	@Schema(name = "quantity", description = "Số lượng")
	private int quantity;
//	@Schema(name = "size", description = "Size của 1 sản phẩm sổ gạo VD: 50 (kg)")
//	private int size;
//	@Schema(name = "price", description = "Giá 1 sản phẩm")
//	private int price;
//	@Schema(name = "priceDiscount", description = "Giá 1 sản phẩm sau khuyến mại")
//	private int priceDiscount;
}
