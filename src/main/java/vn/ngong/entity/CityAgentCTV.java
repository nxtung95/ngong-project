package vn.ngong.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "city_agent_ctv")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CityAgentCTV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "order_number")
    private int orderNumber;

    @Column(name = "status")
    private int status;
}
