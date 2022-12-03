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
@Table(name = "feedback")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "description")
	private String description;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "status")
	private int status;

	@Column(name = "order_number")
	private String orderNumber;

}
