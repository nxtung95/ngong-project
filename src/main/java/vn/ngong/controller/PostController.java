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
import vn.ngong.entity.Post;
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
					"\n 1. SO_LUOC_VE_NGONG" +
					"\n 2. BAO_CHI_NOI_GI_VE_NGONG" +
					"\n 3. HE_THONG_SX_VA_QL" +
					"\n 4. CHUYEN_DI_CUA_NGONG" +
					"\n 5. DU_AN_KHAC" +
					"\n 6. CHUYEN_NGONG" +
					"\n 7. CHUYEN_XUONG" +
					"\n 8. CHUYEN_VUON" +
					"\n 9. CHUYEN_TIEU_DUNG" +
					"\n 10. CHINH_SACH_DAI_LY_CTV" +
					"\n 11. HE_THONG_DOI_TAC" +
					"\n 12. TUYEN_DUNG" +
					"\n 13. LIEN_HE" +
					"\n 14. MUA_O_DAU")

	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<FindPostByMenuResponse> findPostByMenu(@Parameter(required = true, example = "CHUYEN_NGONG") @RequestParam String menuCode) {
		FindPostByMenuResponse res = FindPostByMenuResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		res.setPost(postService.findPostByMenu(menuCode));
		return ResponseEntity.ok(res);
	}
}
