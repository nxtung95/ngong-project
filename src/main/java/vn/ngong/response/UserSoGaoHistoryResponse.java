package vn.ngong.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.UserSoGaoHistoryDto;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class UserSoGaoHistoryResponse extends BaseResponse {
    private List<UserSoGaoHistoryDto> histories;
}
