package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ngong.response.FindPostByMenuResponse;
import vn.ngong.service.PostService;

@RestController
@Slf4j
@RequestMapping(value = "/posts")
public class PostController {
	@Autowired
	private PostService postService;

	@Operation(summary = "API lấy danh sách bài viết của một menu",
			description = "Trường code: \n 00: Thành công." +
					"\n Các giá trị menu code:" +
					"\n 1. chuyen-di-cua-ngong" +
					"\n 2. du-an-khac" +
					"\n 3. chuyen-ngong" +
					"\n 4. chuyen-xuong" +
					"\n 5. chuyen-vuon" +
					"\n 6. chuyen-tieu-dung")

	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<FindPostByMenuResponse> findPostByMenu(
			@Parameter(required = true, example = "chuyen-ngong", description = "Menu dạng slug") @RequestParam(name = "menuCode") String menuCode,
			@Parameter(required = true, description = "curent page") @RequestParam(name = "pageIndex") String pageIndex,
			@Parameter(required = true, description = "page size") @RequestParam(name = "pageSize") String pageSize) {
		FindPostByMenuResponse res = FindPostByMenuResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		int pgIndex = Integer.parseInt(pageIndex);
		int limit = Integer.parseInt(pageSize);
		int offset = (pgIndex - 1) * limit;
		res.setPostList(postService.findPostByMenu(menuCode, limit, offset));
		return ResponseEntity.ok(res);
	}
}
