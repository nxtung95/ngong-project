package vn.ngong.dto;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@Builder(toBuilder = true)
public class ProductVariantDto {
    private int id;

    private int productId;

    private String code;

    private String name;

    private int size;

    private String unit;

    private double weight;

    private int price;

    private int salePrice;

    private String productImages;

    private Object variantDetail;

    @Builder.Default
    private int quantity = 100;
}
