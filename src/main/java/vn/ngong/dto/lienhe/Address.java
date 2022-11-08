package vn.ngong.dto.lienhe;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
	private String key;
	private String address;
	private int status;
	private int order;
	private int latitude;
	private int longitude;
}
