package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.GetListOrderDto;
import vn.ngong.dto.OrderDto;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class GetListOrderResponse extends BaseResponse {
    private GetListOrderDto data;
}
