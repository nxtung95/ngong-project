package vn.ngong.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductFilterRequest {
    private  int categoryId;
    private String brandName;
    private long minPrice;
    private long maxPrice;
    private String productName;
    private int orderType;
    private int pageIndex;
    private int pageSize;
}
