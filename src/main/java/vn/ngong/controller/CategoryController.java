package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.ngong.entity.Category;
import vn.ngong.request.GetConfigRequest;
import vn.ngong.response.CategoryResponse;
import vn.ngong.response.GetConfigResponse;
import vn.ngong.service.CategoryService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/product_categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "API Lấy danh sách danh mục sản phẩm",
            description = "Trường code: \n 00: Thành công")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> list() throws Exception {
        List<Category> categories = categoryService.list();
        CategoryResponse res = CategoryResponse.builder()
                .productCategories(categories)
                .code("00")
                .desc("Success")
                .build();
        return ResponseEntity.ok(res);
    }
}
