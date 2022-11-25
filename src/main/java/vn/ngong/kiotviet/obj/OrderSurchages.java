package vn.ngong.kiotviet.obj;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderSurchages {
    private int id;
    private String code;
    private String price;
}
