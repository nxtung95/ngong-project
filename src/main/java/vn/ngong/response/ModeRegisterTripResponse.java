package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.lienhe.Address;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class ModeRegisterTripResponse extends BaseResponse {
	private int mode;
}
