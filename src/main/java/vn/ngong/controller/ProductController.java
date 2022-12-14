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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.ngong.dto.ProductDto;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.kiotviet.response.DetailProductKiotVietResponse;
import vn.ngong.kiotviet.service.GetDetailProductService;
import vn.ngong.response.DetailProductResponse;
import vn.ngong.service.ProductService;

@RestController
@Slf4j
public class ProductController {
	@Autowired
	private GetDetailProductService getDetailProductService;

	@Autowired
	private ProductService productService;

	@Operation(summary = "API lấy chi tiết sản phẩm theo mã sản phẩm", description = "Code\n 02:Lỗi truy vấn sản phẩm, \n03: Mã sản phẩm không tồn tại bên KiotViet")
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<DetailProductResponse> findProductByCode(@Parameter(required = true, example = "SP000001") @RequestParam String productCode) {
		DetailProductResponse res = DetailProductResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (ValidtionUtils.checkEmptyOrNull(productCode)) {
			res.setCode("01");
			res.setDesc("Invalid request");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		DetailProductKiotVietResponse kiotVietResponse = getDetailProductService.getDetailProductByCode(productCode);
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
