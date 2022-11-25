package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.ngong.dto.CartDto;
import vn.ngong.dto.UserSoGaoHistoryDto;
import vn.ngong.entity.User;
import vn.ngong.helper.AuthenticationUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.response.CartListResponse;
import vn.ngong.response.UserSoGaoHistoryResponse;
import vn.ngong.service.UserSoGaoHistoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/so-gao-history")
public class SoGaoHistoryController {

    @Autowired
    private AuthenticationUtil authenticationUtil;
    @Autowired
    private UserSoGaoHistoryService userSoGaoHistoryService;

    @Operation(summary = "API get danh sách lịch sử sổ gạo",
            description = "Trường code: \n 00: Thành công")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<UserSoGaoHistoryResponse> list(HttpServletRequest httpServletRequest) throws Exception {
        int userId = 0;
        String token = authenticationUtil.extractTokenFromRequest(httpServletRequest);
        if (!ValidtionUtils.checkEmptyOrNull(token)) {
            User user = authenticationUtil.getUserFromToken(token);
            userId = user.getId();
        }

        List<UserSoGaoHistoryDto> histories = userSoGaoHistoryService.list(userId);
        UserSoGaoHistoryResponse res = UserSoGaoHistoryResponse
                .builder()
                .histories(histories)
                .code("00")
                .desc("Success")
                .build();
        return ResponseEntity.ok(res);
    }
}