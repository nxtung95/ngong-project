package vn.ngong.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartInsertRequest {
    private int userId;
    private int productId;
    private int quantity;
}