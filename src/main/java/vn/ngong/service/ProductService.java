package vn.ngong.service;

import vn.ngong.request.ProductFilterRequest;
import vn.ngong.response.ProductFilterResponse;

public interface ProductService {
    ProductFilterResponse list(ProductFilterRequest request);
}
