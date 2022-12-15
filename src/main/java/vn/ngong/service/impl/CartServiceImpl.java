package vn.ngong.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.dto.CartDto;
import vn.ngong.dto.ProductVariantDto;
import vn.ngong.entity.Cart;
import vn.ngong.entity.Product;
import vn.ngong.entity.ProductVariant;
import vn.ngong.kiotviet.obj.Attribute;
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
    @Autowired
    private Gson gson;

    @Override
    public List<CartDto> list(int userId){
        List<Cart> carts = cartRepository.findAllByUserId(userId);
        List<CartDto> res = new ArrayList<>();
        for (Cart cart : carts) {
            ProductVariant variant = productVariantRepository.findById(cart.getProductVariantId()).get();
            List<ProductVariant> variants = productVariantRepository.findAllByProductIdAndStatus(variant.getProductId(), 1);
            List<ProductVariantDto> variantDtos = new ArrayList<>();
            for (ProductVariant p : variants) {
                ProductVariantDto dto = ProductVariantDto
                        .builder()
                        .id(p.getId())
                        .code(p.getCode())
                        .productId(p.getProductId())
                        .name(p.getName())
                        .price(p.getPrice())
                        .salePrice(p.getSalePrice())
                        .saleRate(p.getSalePrice() * 100 / p.getPrice())
                        .productImages(gson.fromJson(p.getProductImages() == null ? "" : p.getProductImages(), new TypeToken<List<String>>(){}.getType()))
                        .variantDetail(gson.fromJson(p.getVariantDetail() == null ? "" : p.getVariantDetail(), Object.class))
                        .weight(p.getWeight())
                        .build();
                variantDtos.add(dto);
            }
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
                    .weight(variant.getWeight())
                    .productVariants(variantDtos)
                    .attributes(gson.fromJson(product.getAttributes() == null ? "" : product.getAttributes(), new TypeToken<List<Attribute>>(){}.getType()))
                    .build();

            res.add(dto);
        }

        return res;
    }

    @Override
    public Cart insert(CartInsertRequest cart) {
        Cart entity = cartRepository.findAllByUserIdAndProductVariantId(cart.getUserId(), cart.getProductVariantId());

        if (entity != null) {
            entity.setQuantity(cart.getQuantity());
            entity.setUpdatedAt(Timestamp.from(Instant.now()));
        }
        else {
            entity = new Cart().toBuilder()
                    .userId(cart.getUserId())
                    .productVariantId(cart.getProductVariantId())
                    .quantity(cart.getQuantity())
                    .createdAt(Timestamp.from(Instant.now()))
                    .createdBy(cart.getUserId())
                    .updatedAt(Timestamp.from(Instant.now()))
                    .updatedBy(cart.getUserId())
                    .build();
        }
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
