package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.Category;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class CategoryResponse extends BaseResponse {
    private List<Category> productCategories;
}
