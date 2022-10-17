package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
    private ProductService productService;

    @Operation(summary = "API lấy danh sách sản phẩm",
            description = "")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Thất bại", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductFilterResponse> list(@RequestBody ProductFilterRequest rq) throws Exception {
        try {
            ProductFilterResponse products = productService.list(rq);

            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
