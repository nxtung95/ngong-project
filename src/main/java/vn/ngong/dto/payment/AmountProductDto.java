package vn.ngong.dto.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AmountProductDto {
	@Schema(name = "productId", description = "ID sản phẩm")
	private int productId;
	@Schema(name = "productCode", description = "Mã sản phẩm")
	private String productCode;
	@Schema(name = "size", description = "Size của 1 sản phẩm gạo VD: 50 (kg)")
	private int size;
	@Schema(name = "price", description = "Giá 1 sản phẩm")
	private int price;
	@Schema(name = "priceDiscount", description = "Giá 1 sản phẩm sau khuyến mại")
	private int priceDiscount;
	@Schema(name = "gaoFlag", description = "0:sản phẩm không phải gạo, 1: sản phẩm gạo")
	private int gaoFlag;

	private Integer quantity;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer soGaoFlag;

	private String productName;
}
