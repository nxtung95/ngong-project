package vn.ngong.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class ProductFilterDetail {
    private int id;
    private String name;
    private BigDecimal price;
    private String image;
}
