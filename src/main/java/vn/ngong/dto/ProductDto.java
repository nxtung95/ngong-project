package vn.ngong.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.kiotviet.obj.Attribute;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class ProductDto {
	private String name;
	private String code;
	private String brandName;
	private String price;
	private String description;
	@Schema(name = "soGaoFlag", description = "0: Không phải sổ gạo, 1: Là sổ gạo")
	private int soGaoFlag;
	@Schema(name = "attributeList", description = "Danh sách thuộc tính của sản phẩm")
	private List<Attribute> attributeList;
	@Schema(name = "tồn kho", description = "Số lượng tồn kho của sản phẩm")
	private int onHand; //ton kho
	List<ProductVariantDto> productVariants;
}
