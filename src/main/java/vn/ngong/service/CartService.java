package vn.ngong.service;

import vn.ngong.entity.Cart;
import vn.ngong.request.CartInsertRequest;
import vn.ngong.request.CartUpdateRequest;

public interface CartService {
    Cart insert(CartInsertRequest cart);
    Cart update(CartUpdateRequest cart);
    void delete(int id);
}
