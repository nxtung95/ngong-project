package vn.ngong.service;

import vn.ngong.dto.ProductDto;
import vn.ngong.kiotviet.response.DetailProductKiotVietResponse;

public interface ProductService {
	ProductDto getProductDetail(String code, DetailProductKiotVietResponse detailProductKiotViet);

	boolean checkInventory(String code);
}
