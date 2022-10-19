package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.ngong.dto.ProductDto;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.kiotviet.response.DetailProductKiotVietResponse;
import vn.ngong.kiotviet.service.KiotVietService;
import vn.ngong.response.DetailProductResponse;
import vn.ngong.request.ProductFilterRequest;
import vn.ngong.request.RegisterRequest;
import vn.ngong.response.ProductFilterDetail;
import vn.ngong.response.ProductFilterResponse;
import vn.ngong.response.RegisterResponse;
import vn.ngong.service.ProductService;
import vn.ngong.service.UserService;

import java.util.List;

@RestController
@Slf4j
public class ProductController {
	@Autowired
	private KiotVietService kiotVietService;

	@Autowired
	private ProductService productService;

	@Operation(summary = "API lấy danh sách sản phẩm", description = "brancName: Thương hiệu\nproductName: Tên sản phẩm\ncategoryId: Danh mục sản phẩm\norderType: 0 - giá tăng dần, 1 - giá giảm dần\nminPrice, maxPrice: khoản giá, truyền maxPrice = -1 cho trường hợp tất cả")
	@RequestMapping(value = "product", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductFilterResponse> list(@RequestBody ProductFilterRequest rq) throws Exception {
        try {
            ProductFilterResponse products = productService.list(rq);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Operation(summary = "API lấy chi tiết sản phẩm theo mã sản phẩm", description = "Code\n 02:Lỗi truy vấn sản phẩm, \n03: Mã sản phẩm không tồn tại bên KiotViet")
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "400", description = "Thất bại", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@RequestMapping(value = "products/{productCode}", method = RequestMethod.GET)
	public ResponseEntity<DetailProductResponse> findProductByCode(@Parameter(required = true, example = "SP000001") @PathVariable String productCode) {
		DetailProductResponse res = DetailProductResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (ValidtionUtils.checkEmptyOrNull(productCode)) {
			res.setCode("01");
			res.setDesc("Invalid request");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		DetailProductKiotVietResponse kiotVietResponse = kiotVietService.getDetailProductByCode(productCode);
		if (kiotVietResponse == null) {
			res.setCode("02");
			res.setDesc("Có lỗi truy vấn sản phẩm, vui lòng thử lại sau...");
			return new ResponseEntity<>(res, HttpStatus.BAD_GATEWAY);
		}
		if (kiotVietResponse.getResponseStatus() != null
				&& !ValidtionUtils.checkEmptyOrNull(kiotVietResponse.getResponseStatus().getErrorCode())) {
			res.setCode("03");
			res.setDesc(kiotVietResponse.getResponseStatus().getMessage());
			return new ResponseEntity<>(res, HttpStatus.BAD_GATEWAY);
		}
		ProductDto productDto = productService.getProductDetail(productCode, kiotVietResponse);
		if (productDto == null) {
			res.setCode("02");
			res.setDesc("Có lỗi truy vấn sản phẩm, vui lòng thử lại sau...");
			return new ResponseEntity<>(res, HttpStatus.BAD_GATEWAY);
		}

		res.setDetailProduct(productDto);
		return ResponseEntity.ok(res);
	}
}
