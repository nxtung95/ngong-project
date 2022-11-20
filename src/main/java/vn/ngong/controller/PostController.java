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
import vn.ngong.response.*;
import vn.ngong.service.PostService;
import vn.ngong.service.UtilityService;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping(value = "/posts")
public class PostController {
	@Autowired
	private PostService postService;
	@Autowired
	private UtilityService utilityService;

	@Operation(summary = "API lấy danh sách bài viết của một menu",
			description = "Trường code: \n 00: Thành công." +
					"\n Các giá trị menu code:" +
					"\n 1. tin--tuc" +
					"\n 2. chuyen-ngong" +
					"\n 3. chuyen-xuong" +
					"\n 4. chuyen-vuon" +
					"\n 5. chuyen-tieu-dung" +
					"\n 6. chinh-sach-dai-ly-ctv" +
					"\n\n Response:" +
					"\n1. postContent: Nội dung chi tiết bài viết" +
					"\n2. postTitle: Title đại diện bài viết" +
					"\n3. menuImage: Image đại diện bài viết" +
					"\n4. description: Mô tả đại diện bài viết" +
					"\n\nĐối với lấy 4 tin tức nổi bật:" +
					"\n - Truyền menuCode=tin-tuc" +
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

	@Operation(summary = "API lấy danh sách bài viết có số lượt xem nhiều nhất")
	@RequestMapping(value = "/topPost", method = RequestMethod.GET)
	public ResponseEntity<FindPostByMenuResponse> findLastestCountViewPost(@RequestParam(name = "size") int size, @RequestParam(name = "orderBy") int orderBy) {
		FindPostByMenuResponse res = FindPostByMenuResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (orderBy != 0) {
			res.setPostList(postService.findLastTopPostOrderByMonth(size, orderBy));
		} else {
			res.setPostList(postService.findLastTopPost(size));
		}
		return ResponseEntity.ok(res);
	}

	@Operation(summary = "API lấy về nội dung bài viết sơ lược về ngỗng")
	@RequestMapping(value = "/aboutNgong", method = RequestMethod.GET)
	public ResponseEntity<SoLuocVeNgongResponse> getContentSoLuocVeNgong() {
		SoLuocVeNgongResponse res = SoLuocVeNgongResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		res.setSoLuocVeNgong(utilityService.getSoLuocVeNgongContent());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API lấy về nội dung dự án chuyến đi của Ngỗng")
	@RequestMapping(value = "/project/chuyenDiCuaNgong", method = RequestMethod.GET)
	public ResponseEntity<ChuyenDiCuaNgongResponse> getContentChuyenDiCuaNgong() {
		ChuyenDiCuaNgongResponse res = ChuyenDiCuaNgongResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		res.setChuyenDiCuaNgong(utilityService.getChuyenDiCuaNgongContent());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API lấy về nội dung trang chủ dự án")
	@RequestMapping(value = "/project/trangChu", method = RequestMethod.GET)
	public ResponseEntity<TrangChuDuAnResponse> getContentTrangChuDuAn() {
		TrangChuDuAnResponse res = TrangChuDuAnResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		res.setTrangChuDuAn(utilityService.getTrangChuDuAnContent());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API lấy về nội dung dự án cánh đồng sẻ chia")
	@RequestMapping(value = "/project/canhDongSeChia", method = RequestMethod.GET)
	public ResponseEntity<CanhDongSeChiaResponse> getContentCanhDongSeChia() {
		CanhDongSeChiaResponse res = CanhDongSeChiaResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		res.setCanhDongSeChia(utilityService.getCanhDongSeChiaContent());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API lấy về nội dung trang tuyển dụng news")
	@RequestMapping(value = "/tuyenDungNews", method = RequestMethod.GET)
	public ResponseEntity<TuyenDungNewsResponse> getContentTuyenDungNews() {
		TuyenDungNewsResponse res = TuyenDungNewsResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		res.setTuyenDungNews(utilityService.getTuyenDungNewsContent());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API lấy nội dung trang liên hệ")
	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public ResponseEntity<ContactResponse> getAddress() {
		ContactResponse res = ContactResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		res.setAddressList(utilityService.getAddress());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API lấy nội dung trang Mua Ở Đâu")
	@RequestMapping(value = "/buyWhere", method = RequestMethod.GET)
	public ResponseEntity<MuaODauContentResponse> getMuaODauContent() {
		MuaODauContentResponse res = MuaODauContentResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		res.setMuaODau(utilityService.getMuaODauContent());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API lấy nội dung trang Footer")
	@RequestMapping(value = "/footer", method = RequestMethod.GET)
	public ResponseEntity<FooterContentResponse> getFooterContent() {
		FooterContentResponse res = FooterContentResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		res.setFooter(utilityService.getFooterContent());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
