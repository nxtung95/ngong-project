package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.sogao.ReturnUserSoGaoHistoryDto;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class UserSoGaoHistoryResponse extends BaseResponse {
    private List<ReturnUserSoGaoHistoryDto> histories;
}
