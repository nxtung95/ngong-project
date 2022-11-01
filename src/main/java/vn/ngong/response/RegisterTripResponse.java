package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.RegisterTrip;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class RegisterTripResponse extends BaseResponse {
	private RegisterTrip registerTrip;
}
