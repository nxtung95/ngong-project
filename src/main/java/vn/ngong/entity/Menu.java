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
@Entity
@Table(name = "wp_term_taxonomy")
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "term_taxonomy_id")
	private int id;

	@Column(name = "term_id")
	private int termId;

	@Column(name = "taxonomy")
	private String taxonomy;

	@Column(name = "description")
	private String description;

	@Column(name = "parent")
	private int parent;

	@Column(name = "count")
	private int count;
}
