package vn.ngong.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartUpdateRequest extends CartInsertRequest {
    private int id;
}
