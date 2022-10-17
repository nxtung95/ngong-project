package vn.ngong.service;

import vn.ngong.dto.ProductDto;
import vn.ngong.kiotviet.response.DetailProductKiotVietResponse;
import vn.ngong.request.ProductFilterRequest;
import vn.ngong.response.ProductFilterResponse;

public interface ProductService {
	ProductDto getProductDetail(String code, DetailProductKiotVietResponse detailProductKiotViet);
    ProductFilterResponse list(ProductFilterRequest request);
}
