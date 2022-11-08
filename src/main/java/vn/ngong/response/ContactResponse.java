package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.ThanhTuuVaGiaiThuong;
import vn.ngong.dto.lienhe.Address;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class ContactResponse extends BaseResponse {
	private List<Address> addressList;
}
