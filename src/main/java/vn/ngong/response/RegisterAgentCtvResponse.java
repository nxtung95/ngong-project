package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.RegisterAgentCTV;
import vn.ngong.entity.RegisterTrip;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class RegisterAgentCtvResponse extends BaseResponse {
	private RegisterAgentCTV register;
}
