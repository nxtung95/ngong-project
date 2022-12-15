package vn.ngong.dto;

import lombok.*;
import vn.ngong.kiotviet.obj.Attribute;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private int id;
    private int productId;
    private String productName;
    private int productVariantId;
    private String productVariantName;
    private int quantity;
    private int price;
    private int sale_prices;
    private double weight;
    private String images;
    private List<ProductVariantDto> productVariants;
    private List<Attribute> attributes;
}
