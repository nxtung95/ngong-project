package vn.ngong.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Builder
@Getter
@Setter
public class ProductFilterDetail {
    private int id;
    private String name;
    private long price;
    private String image;
    private int saleRate;
    private String saleName;
    private long salePrice;
    private Timestamp saleStartTime;
    private Timestamp saleEndTime;
}
