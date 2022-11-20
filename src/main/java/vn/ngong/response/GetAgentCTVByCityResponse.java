package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.AgentCTV;
import vn.ngong.entity.CityAgentCTV;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class GetAgentCTVByCityResponse extends BaseResponse {
	private List<AgentCTV> agentCTVList;
}