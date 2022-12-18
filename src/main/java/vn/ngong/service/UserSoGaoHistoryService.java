package vn.ngong.service;

import vn.ngong.dto.sogao.ReturnUserSoGaoHistoryDto;

import java.util.List;

public interface UserSoGaoHistoryService {
    List<ReturnUserSoGaoHistoryDto> list(int userId);
}
