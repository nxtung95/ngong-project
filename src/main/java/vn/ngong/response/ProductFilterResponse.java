package vn.ngong.response;

import java.util.List;

public class ProductFilterResponse extends BaseResponse{
    private List<ProductFilterDetail> products;
    private int pageIndex;
    private int pageSize;
    private int totalItem;
}
