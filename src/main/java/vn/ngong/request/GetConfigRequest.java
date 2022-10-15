package vn.ngong.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetConfigRequest {
	@Schema(name = "key", description = "list những key, ngăn cách bởi dấu phẩy", example = "hotline,slide", required = true)
	private String keys;
}
