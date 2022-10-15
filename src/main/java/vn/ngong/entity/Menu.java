package vn.ngong.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class Menu {
	private int id;
	private String code;
	private String name;

	private List<Post> postList;
}
