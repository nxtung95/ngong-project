package vn.ngong.service;

import vn.ngong.dto.CartDto;
import vn.ngong.entity.Cart;
import vn.ngong.request.CartInsertRequest;
import vn.ngong.request.CartUpdateRequest;

import java.util.List;

public interface CartService {
    List<CartDto> list(int userId);
    Cart insert(CartInsertRequest cart);
    Cart update(CartUpdateRequest cart);
    void delete(int id);
}
