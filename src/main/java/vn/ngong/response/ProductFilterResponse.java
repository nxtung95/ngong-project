package vn.ngong.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@Jacksonized
public class ProductFilterResponse extends BaseResponse {
    private List<ProductFilterDetail> products;
    private int pageIndex;
    private int pageSize;
    private int totalItem;
}
