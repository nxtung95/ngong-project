package vn.ngong.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.SystemParameter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class GetConfigResponse extends BaseResponse {
	@Schema(required = true, description = "Danh sách cấu hình trả về theo key")
	private List<Map<String, String>> systemParameters;
}
