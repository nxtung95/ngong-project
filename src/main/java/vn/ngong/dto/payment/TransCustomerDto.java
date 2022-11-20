package vn.ngong.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransCustomerDto {
	private String cusName;
	private String cusEmail;
	private String cusPhone;
	private String cusCity;
	private String cusDistrict;
	private String cusWard;
	private String cusNote;
}
