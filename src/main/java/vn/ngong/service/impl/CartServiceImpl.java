package vn.ngong.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.dto.CartDto;
import vn.ngong.entity.Cart;
import vn.ngong.entity.Product;
import vn.ngong.entity.ProductVariant;
import vn.ngong.repository.CartRepository;
import vn.ngong.repository.ProductRepository;
import vn.ngong.repository.ProductVariantRepository;
import vn.ngong.request.CartInsertRequest;
import vn.ngong.request.CartUpdateRequest;
import vn.ngong.service.CartService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Override
    public List<CartDto> list(int userId){
        List<Cart> carts = cartRepository.findAllByUserId(userId);
        List<CartDto> res = new ArrayList<>();
        for (Cart cart : carts) {
            ProductVariant variant = productVariantRepository.findById(cart.getProductVariantId()).get();
            Product product = productRepository.findById(variant.getProductId()).get();
            CartDto dto = CartDto
                    .builder()
                    .id(cart.getId())
                    .productId(product.getId())
                    .productName(product.getName())
                    .productVariantId(variant.getId())
                    .productVariantName(variant.getName())
                    .price(variant.getPrice())
                    .sale_prices(variant.getSalePrice())
                    .quantity(cart.getQuantity())
                    .images(variant.getProductImages())
                    .weight(variant.getWeight() * cart.getQuantity())
                    .build();

            res.add(dto);
        }

        return res;
    }

    @Override
    public Cart insert(CartInsertRequest cart) {
        Cart entity = new Cart().toBuilder()
                .userId(cart.getUserId())
                .productVariantId(cart.getProductVariantId())
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
