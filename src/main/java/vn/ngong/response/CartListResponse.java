package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.Cart;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class CartListResponse extends BaseResponse {
    private List<Cart> carts;
}
