package vn.ngong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum UserTypeEnum {
	USER("0"),
	ADMIN("1");

	private final String label;

	public String label() {
		return label;
	}
}
