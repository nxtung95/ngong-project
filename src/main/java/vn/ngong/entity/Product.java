package vn.ngong.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class Product implements Serializable {
    private int id;
    private String code;
    private String name;
    @Schema(name = "brand_name")
    private String brandName;
    private double price;
    private String description;
    @Schema(name = "so_gao_flag")
    private boolean soGaoFlag;
    private boolean status;
    @Schema(name = "created_at")
    private Timestamp createdAt;
    @Schema(name = "created_by")
    private int createdBy;
    @Schema(name = "updated_at")
    private Timestamp updateAt;
    @Schema(name = "updated_by")
    private int updatedBy;
}
