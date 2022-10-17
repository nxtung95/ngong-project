package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.ProductDto;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class DetailProductResponse extends BaseResponse {
	private ProductDto detailProduct;
}
