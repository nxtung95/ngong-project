package vn.ngong.dto.sogao;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnUserSoGaoHistoryDto {
    private String soGaoCode;
    private String soGaoName;
    private String purchaseDate;
    private int remainingNumber;
    private List<UsedSoGaoDto> usedList;
}
