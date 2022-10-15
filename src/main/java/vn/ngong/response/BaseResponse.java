package vn.ngong.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BaseResponse implements Serializable {
	@Schema(name = "code", description = "00: Thành công, khác 00: Có lỗi")
	private String code;
	@Schema(name = "desc", description = "Message lỗi hiển thị")
	private String desc;
}
