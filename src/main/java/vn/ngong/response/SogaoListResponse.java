package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.Sogao;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class SogaoListResponse extends BaseResponse {
    List<Sogao> sogaos;
}
