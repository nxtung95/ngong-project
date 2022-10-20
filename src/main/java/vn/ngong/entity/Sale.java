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
@Table(name = "product_sales")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "rate")
    private double rate ;

    @Column(name = "sale_price")
    private long salePrice ;

    @Column(name = "start_time")
    private Timestamp startTime ;

    @Column(name = "end_time")
    private Timestamp endTime ;

    @Column(name = "status")
    private int status ;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private int createdBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "updated_by")
    private int updatedBy;
}