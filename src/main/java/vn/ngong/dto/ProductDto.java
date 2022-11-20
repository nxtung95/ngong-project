package vn.ngong.dto;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.entity.ProductVariant;
import vn.ngong.kiotviet.obj.Attribute;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder(toBuilder = true)
public class ProductDto {
	private String name;
	private String code;
	private String brandName;
	private String price;
	private String description;
	private int categoryId;
	private String nutrition;
	private String origin;
	private String productImages;
	private List<Attribute> attributes;
	@Schema(name = "soGaoFlag", description = "0: Không phải sổ gạo, 1: Là sổ gạo")
	private int soGaoFlag;
//	@Schema(name = "attributeList", description = "Danh sách thuộc tính của sản phẩm")
//	private List<Attribute> attributeList;
//	@Schema(name = "tồn kho", description = "Số lượng tồn kho của sản phẩm")
//	private int onHand; //ton kho
	List<ProductVariantDto> productVariants;
}
