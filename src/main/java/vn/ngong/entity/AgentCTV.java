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
@Table(name = "agent_ctv")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AgentCTV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type")
    private int type;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "city_agent_ctv_id")
    private int cityAgenCTVId;

    @Column(name = "status")
    private int status;

    @Column(name = "order_number")
    private int orderNumber;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "name_url")
    private String nameUrl;
}
