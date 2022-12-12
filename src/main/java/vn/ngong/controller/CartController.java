package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.ngong.dto.CartDto;
import vn.ngong.entity.Cart;
import vn.ngong.entity.User;
import vn.ngong.helper.AuthenticationUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.request.CartInsertRequest;
import vn.ngong.request.CartUpdateRequest;
import vn.ngong.response.CartInsertResponse;
import vn.ngong.response.CartListResponse;
import vn.ngong.response.CategoryResponse;
import vn.ngong.service.CartService;
import vn.ngong.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/carts")
@CrossOrigin(origins = "*")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private AuthenticationUtil authenticationUtil;

    @Operation(summary = "API get danh sách giỏ hàng",
            description = "Trường code: \n 00: Thành công")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<CartListResponse> list(HttpServletRequest httpServletRequest) throws Exception {
        int userId = 0;
        String token = authenticationUtil.extractTokenFromRequest(httpServletRequest);
        if (!ValidtionUtils.checkEmptyOrNull(token)) {
            User user = authenticationUtil.getUserFromToken(token);
            userId = user.getId();
        }
        List<CartDto> carts = cartService.list(userId);
        CartListResponse res = CartListResponse
                .builder()
                .carts(carts)
                .code("00")
                .desc("Success")
                .build();
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "API thêm sản phẩm vào giỏ hàng",
            description = "Trường code: \n 00: Thành công")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<CartInsertResponse> insert(@RequestBody CartInsertRequest cart, HttpServletRequest httpServletRequest) throws Exception {        int userId = 0;
        String token = authenticationUtil.extractTokenFromRequest(httpServletRequest);
        if (!ValidtionUtils.checkEmptyOrNull(token)) {
            User user = authenticationUtil.getUserFromToken(token);
            userId = user.getId();
        }
        cart.setUserId(userId);
        Cart entity = cartService.insert(cart);
        CartInsertResponse res = CartInsertResponse
                .builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .productVarianrId(entity.getProductVariantId())
                .quantity(entity.getQuantity())
                .code("00")
                .desc("Success")
                .build();
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "API cập nhật giỏ hàng",
            description = "Trường code: \n 00: Thành công")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<CartInsertResponse> update(@RequestBody CartUpdateRequest cart, HttpServletRequest httpServletRequest) throws Exception {
        int userId = 0;
        String token = authenticationUtil.extractTokenFromRequest(httpServletRequest);
        if (!ValidtionUtils.checkEmptyOrNull(token)) {
            User user = authenticationUtil.getUserFromToken(token);
            userId = user.getId();
        }
        cart.setUserId(userId);
        Cart entity = cartService.update(cart);
        CartInsertResponse res = CartInsertResponse
                .builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .productVarianrId(entity.getProductVariantId())
                .quantity(entity.getQuantity())
                .code("00")
                .desc("Success")
                .build();
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "API xóa sản phẩm trong giỏ hàng",
            description = "Trường code: \n 00: Thành công")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> delete(@RequestParam int id) throws Exception {
        cartService.delete(id);
        return ResponseEntity.ok(true);
    }
}
