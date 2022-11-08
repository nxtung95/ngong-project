package vn.ngong.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "ships")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ship_area_id")
    private int shipAreaId;

    @Column(name = "start_kg")
    private int startKg;

    @Column(name = "end_kg")
    private int endKg;

    @Column(name = "start_money")
    private int startMoney;

    @Column(name = "end_money")
    private int endMoney;

    @Column(name = "price")
    private int price;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at")
    private int createdAt;

    @Column(name = "created_by")
    private int createdBy;

    @Column(name = "updated_at")
    private int updatedAt;

    @Column(name = "updated_by")
    private int updatedBy;
}
