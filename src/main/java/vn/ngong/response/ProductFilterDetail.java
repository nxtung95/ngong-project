package vn.ngong.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.dto.ProductVariantDto;
import vn.ngong.kiotviet.obj.Attribute;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@Setter
public class ProductFilterDetail {
    private int id;
    private String name;
    private String brandName;
    private String origin;
    private int categoryId;
    private int soGaoFlag;
    private String price;
    private List<Object> image;
    private int saleRate;
    private String saleName;
    private String salePrice;
    private List<ProductVariantDto> productVariants;
    private int selledNumber;
    private List<Attribute> attributes;
}
