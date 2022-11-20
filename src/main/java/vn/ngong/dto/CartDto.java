package vn.ngong.dto;

import lombok.*;

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
}
