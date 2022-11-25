package vn.ngong.dto;

import lombok.*;

//@Getter
//@Setter
//@Builder(toBuilder = true)
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public interface UserSoGaoHistoryDto {
//    private int id;
//    private int userId;
//    private int soGaoId;
//    private String soGaoCode;
//    //private int size;
//    private String soGaoName;
//    private int usedNumber;
//    private int remainingNumber;
//    private int status;

    Integer getId();
    Integer getUserId();
    Integer getSoGaoId();
    String getSoGaoCode();
    //private int size;
    String getSoGaoName();
    Integer getUsedNumber();
    Integer getRemainingNumber();
    Integer getStatus();
}
