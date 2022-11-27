package vn.ngong.kiotviet.obj;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderDetail {
    private long productId;
    private String productCode;
    private String productName;
    private double quantity;
    private double price;
    private double discount;
    private double discountRatio;
    private String note;


    private String productImage;
}
