package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.dto.UserSoGaoHistoryDto;
import vn.ngong.repository.UserSoGaoHistoryRepository;
import vn.ngong.service.UserSoGaoHistoryService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserSoGaoHistoryImpl implements UserSoGaoHistoryService {
    @Autowired
    private UserSoGaoHistoryRepository userSoGaoHistoryRepository;

    @Override
    public List<UserSoGaoHistoryDto> list(int userId) {
        try {
            return userSoGaoHistoryRepository.list(userId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }
}
