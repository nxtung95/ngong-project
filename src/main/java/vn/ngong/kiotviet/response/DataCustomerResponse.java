package vn.ngong.kiotviet.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class DataCustomerResponse {
	private long id;
	private String code;
	private String name;
	private int type;
	private boolean gender;
	private Date birthDate;
	private String contactNumber;
	private String address;
	private String locationName;
	private String email;
	private String organization;
	private String comments;
	private String taxCode;
	private int retailerId;
	private String modifiedDate;
	private String createdDate;
	private List<Object> customerGroupDetails;
}
