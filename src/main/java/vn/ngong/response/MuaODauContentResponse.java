package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.muaodau.MuaODauDto;
import vn.ngong.entity.AgentCTV;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class MuaODauContentResponse extends BaseResponse {
	private MuaODauDto muaODau;
}
