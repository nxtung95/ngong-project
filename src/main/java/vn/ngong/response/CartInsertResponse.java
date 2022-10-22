package vn.ngong.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class CartInsertResponse extends BaseResponse {
    private int id;
    private int userId;
    private int productId;
    private int quantity;
}
