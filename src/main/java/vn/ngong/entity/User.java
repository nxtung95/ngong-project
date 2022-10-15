package vn.ngong.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class User implements Serializable {
	private int id;
	private String name;
	private String phone;
	private String email;
	private String password;
}
