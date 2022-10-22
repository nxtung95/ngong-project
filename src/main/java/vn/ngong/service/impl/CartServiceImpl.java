package vn.ngong.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.entity.Cart;
import vn.ngong.repository.CartRepository;
import vn.ngong.request.CartInsertRequest;
import vn.ngong.request.CartUpdateRequest;
import vn.ngong.service.CartService;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Cart insert(CartInsertRequest cart) {
        Cart entity = new Cart().toBuilder()
                .userId(cart.getUserId())
                .productId(cart.getProductId())
                .quantity(cart.getQuantity())
                .createdAt(Timestamp.from(Instant.now()))
                .createdBy(cart.getUserId())
                .updatedAt(Timestamp.from(Instant.now()))
                .updatedBy(cart.getUserId())
                .build();

        return cartRepository.save(entity);
    }

    @Override
    public Cart update(CartUpdateRequest cart) {
        Cart entity = cartRepository.findById(cart.getId());
        entity.setQuantity(cart.getQuantity());
        entity.setUpdatedAt(Timestamp.from(Instant.now()));

        return cartRepository.save(entity);
    }

    @Override
    public void delete(int id) {
        cartRepository.deleteById(id);
    }
}
