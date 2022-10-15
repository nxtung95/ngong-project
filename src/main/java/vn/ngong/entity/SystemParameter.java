package vn.ngong.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class SystemParameter {
	private int id;
	private String key;
	private String value;
	private String image;
	private int order;
	private String status;
	private Timestamp createAt;
	private Timestamp updateAt;
}
