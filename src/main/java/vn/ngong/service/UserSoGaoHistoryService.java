package vn.ngong.service;

import vn.ngong.dto.UserSoGaoHistoryDto;

import java.util.List;

public interface UserSoGaoHistoryService {
    List<UserSoGaoHistoryDto> list(int userId);
}
