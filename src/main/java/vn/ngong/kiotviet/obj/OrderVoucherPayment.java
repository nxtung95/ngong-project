package vn.ngong.kiotviet.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderVoucherPayment {
    @SerializedName(value = "Method", alternate = {"method"})
    @Builder.Default
    private String method = "Voucher"; // Giá trị mặc định là Voucher (không đổi)

    @SerializedName(value = "MethodStr", alternate = {"methodStr"})
    @Builder.Default
    private String methodStr = "Voucher"; // Giá trị mặc định là Voucher (không đổi)

    @SerializedName(value = "Amount", alternate = {"amount"})
    private Integer amount;  // Giá trị của voucher

    @SerializedName(value = "ID", alternate = {"id"})
    @Builder.Default
    private int id = -1;  // Giá trị mặc định là -1 (không đổi)

    @SerializedName(value = "AccountId", alternate = {"accountId"})
    private Integer accountId; // Giá trị mặc định là null (không đổi)

    @SerializedName(value = "VoucherId", alternate = {"voucherId"})
    private Integer voucherId; // Id của voucher

    @SerializedName(value = "VoucherCampaignId", alternate = {"voucherCampaignId"})
    private Integer voucherCampaignId; // Id của đợt phát hành voucher

}
