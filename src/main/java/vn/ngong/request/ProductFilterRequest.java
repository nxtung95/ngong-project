package vn.ngong.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductFilterRequest {
    private String brandName;
    private int minPrice;
    private int maxPrice;
    private String productName;
    private int orderType;
    private int pageIndex;
    private int pageSize;
}
