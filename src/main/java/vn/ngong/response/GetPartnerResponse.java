package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.PartnerDto;
import vn.ngong.dto.trangchu.ImageQCSoGao;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class GetPartnerResponse extends BaseResponse {
	private List<PartnerDto> partnerList;
}
