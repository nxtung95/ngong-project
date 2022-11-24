package vn.ngong.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.dto.ProductVariantDto;

import java.math.BigDecimal;
import java.sql.Timestamp;

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
    private String image;
    private int saleRate;
    private String saleName;
    private String salePrice;
    private ProductVariantDto productVariant;
}
