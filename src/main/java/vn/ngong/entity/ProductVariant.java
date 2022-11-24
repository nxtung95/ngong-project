package vn.ngong.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "wp_product_variants")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "variants_detail")
    private String variantDetail;

    @Column(name = "weight")
    private double weight;

    @Column(name = "price")
    private int price;

    @Column(name = "sale_prices")
    private int salePrice;

    @Column(name = "product_images")
    private String productImages;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private int createdBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "updated_by")
    private int updatedBy;
}