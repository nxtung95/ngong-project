package vn.ngong.dto;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class ProductVariantDto {
    private int id;

    private int productId;

    private String code;

    private String name;

    private double weight;

    private int price;

    private int salePrice;

    private int saleRate;

    private List<String> productImages;

    private Object variantDetail;

    private int quantity;
}
