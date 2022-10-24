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
import vn.ngong.entity.ViewCountPost;
import vn.ngong.request.UpdateViewCountPostRequest;
import vn.ngong.response.FindPostByMenuResponse;
import vn.ngong.response.UpdateViewCountPostResponse;
import vn.ngong.service.PostService;

import javax.servlet.http.HttpServletRequest;

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
					"\n 6. chuyen-tieu-dung" +
					"\n\n Response:" +
					"\n1. postContent: Nội dung chi tiết bài viết" +
					"\n2. postTitle: Title đại diện bài viết" +
					"\n3. menuImage: Image đại diện bài viết" +
					"\n4. description: Mô tả đại diện bài viết" +
					"\n\nĐối với lấy 4 tin tức nổi bật:" +
					"\n - Truyền menuCode=news" +
					"\n - pageIndex = 1, pageSize = 4")

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

	@Operation(summary = "API cập nhật số lần xem 1 bài viết", description = "")
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ResponseEntity<UpdateViewCountPostResponse> updateViewCountPost(@RequestBody UpdateViewCountPostRequest rq, HttpServletRequest httpServletRequest) {
		UpdateViewCountPostResponse res = UpdateViewCountPostResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (rq.getPostId() <= 0) {
			res.setCode("01");
			res.setDesc("Mã bài viết không hợp lệ");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		ViewCountPost viewCountPost = postService.updateViewCount(rq.getPostId(), httpServletRequest);
		res.setPost(viewCountPost);
		return ResponseEntity.ok(res);
	}

	@Operation(summary = "API lấy danh sách 10 bài viết có số lượt xem nhiều nhất")
	@RequestMapping(value = "/topPost", method = RequestMethod.GET)
	public ResponseEntity<FindPostByMenuResponse> findLastestCountViewPost(@RequestParam(name = "size") int size) {
		FindPostByMenuResponse res = FindPostByMenuResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		res.setPostList(postService.findLastTopPost(size));
		return ResponseEntity.ok(res);
	}
}
