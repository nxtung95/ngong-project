package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.dto.sogao.ReturnUserSoGaoHistoryDto;
import vn.ngong.dto.sogao.UsedSoGaoDto;
import vn.ngong.dto.sogao.UserSoGaoHistoryDto;
import vn.ngong.entity.UserSoGaoHistory;
import vn.ngong.helper.FormatUtil;
import vn.ngong.repository.UserSoGaoHistoryRepository;
import vn.ngong.service.UserSoGaoHistoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserSoGaoHistoryImpl implements UserSoGaoHistoryService {
    @Autowired
    private UserSoGaoHistoryRepository userSoGaoHistoryRepository;

    @Override
    public List<ReturnUserSoGaoHistoryDto> list(int userId) {
        List<ReturnUserSoGaoHistoryDto> returnUserSoGaoHistoryDtoList = new ArrayList<>();
        try {
            List<UserSoGaoHistoryDto> userSoGaoHistoryList = userSoGaoHistoryRepository.list(userId);
            Map<String, List<UserSoGaoHistoryDto>> map = new HashMap<>();

            for (UserSoGaoHistoryDto u : userSoGaoHistoryList) {
                String soGaoCode = u.getSoGaoCode();
                if (map.containsKey(soGaoCode)) {
                    List<UserSoGaoHistoryDto> currentSoGaoList = map.get(soGaoCode);
                    currentSoGaoList.add(u);
                } else {
                    List<UserSoGaoHistoryDto> currentSoGaoList = new ArrayList<>();
                    currentSoGaoList.add(u);
                    map.put(soGaoCode, currentSoGaoList);
                }
            }

            map.entrySet().stream().forEach(e -> {
                List<UserSoGaoHistoryDto> current = e.getValue();
                if (current != null && !current.isEmpty()) {
                    ReturnUserSoGaoHistoryDto returnUserSoGaoHisDto = ReturnUserSoGaoHistoryDto.builder()
                            .soGaoCode(e.getKey())
                            .build();
                    List<UsedSoGaoDto> usedSoGaoList = new ArrayList<>();
                    for (UserSoGaoHistoryDto userSoGaoHistoryDto : current) {
                        if (userSoGaoHistoryDto.getUsedNumber() == 0) {
                            // Mua sổ gạo
                            returnUserSoGaoHisDto.setPurchaseDate(FormatUtil.formatTimestampDate(userSoGaoHistoryDto.getPurchaseDate(), "dd/MM/yyyy"));
                        } else {
                            // Xuất sổ gạo
                            usedSoGaoList.add(UsedSoGaoDto.builder()
                                    .usedDate(FormatUtil.formatTimestampDate(userSoGaoHistoryDto.getCreatedAt(), "dd/MM/yyyy"))
                                    .usedNumber(userSoGaoHistoryDto.getUsedNumber())
                                    .build());
                        }
                    }
                    UserSoGaoHistoryDto last = current.get(current.size() - 1);
                    returnUserSoGaoHisDto.setSoGaoName(last.getSoGaoName());
                    returnUserSoGaoHisDto.setUsedList(usedSoGaoList);
                    returnUserSoGaoHisDto.setRemainingNumber(last.getRemainingNumber());
                    returnUserSoGaoHistoryDtoList.add(returnUserSoGaoHisDto);
                }
            });

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return returnUserSoGaoHistoryDtoList;
    }
}
