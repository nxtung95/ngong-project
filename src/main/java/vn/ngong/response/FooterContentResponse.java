package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.footer.FooterDto;
import vn.ngong.dto.muaodau.MuaODauDto;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class FooterContentResponse extends BaseResponse {
	private FooterDto footer;
}
