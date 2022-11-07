package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ngong.entity.Category;
import vn.ngong.entity.Comment;
import vn.ngong.request.CommentInsertRequest;
import vn.ngong.response.CategoryResponse;
import vn.ngong.response.CommentInsertResponse;
import vn.ngong.response.CommentListResponse;
import vn.ngong.service.CommentService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Operation(summary = "API Lấy danh sách comment",
            description = "orderType: 0 - Sắp xếp mới nhất, 1 - Sắp xếp cũ nhất, 2 - Sắp xếp đánh giá cao nhất, 3 - Sắp xếp đánh giá thấp nhất")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<CommentListResponse> list(@RequestParam int orderType, int pageIndex, int pageSize) throws Exception {
        CommentListResponse res = commentService.list(orderType, pageIndex, pageSize);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "API thêm mới comment",
            description = "")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<CommentInsertResponse> add(@RequestBody CommentInsertRequest req) {
        Comment comment = commentService.add(req);
        CommentInsertResponse res = CommentInsertResponse
                .builder()
                .comment(comment)
                .build();
        res.setCode("00");
        res.setDesc("Success");

        return ResponseEntity.ok(res);
    }
}
