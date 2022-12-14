package vn.ngong.service;

import org.springframework.stereotype.Service;
import vn.ngong.dto.ProductDto;
import vn.ngong.dto.payment.TransProductDto;
import vn.ngong.entity.Product;
import vn.ngong.entity.ProductVariant;
import vn.ngong.kiotviet.response.DetailProductKiotVietResponse;
import vn.ngong.request.ProductFilterRequest;
import vn.ngong.response.ProductFilterResponse;

import java.util.List;

public interface ProductService {
	ProductDto getProductDetail(int id, DetailProductKiotVietResponse detailProductKiotViet);

	Integer getQuantityStockByProductCode(String productCode);

    ProductFilterResponse list(ProductFilterRequest request);

	ProductFilterResponse getBestSeller(int limit);

	ProductFilterResponse getNewestSale(int limit);

	Product findById(int productId);

	Product findByProductCode(String productCode);

	List<String> getBrandNames();

	List<ProductVariant> findAllProductByCode(List<String> productCodeList);
}
