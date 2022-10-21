package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.MenuDto;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class MenuResponse extends BaseResponse{
	private List<MenuDto> menuList;
}
