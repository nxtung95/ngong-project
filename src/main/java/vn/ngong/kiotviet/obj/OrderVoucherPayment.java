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
    private String method; // Giá trị mặc định là Voucher (không đổi)

    @SerializedName(value = "MethodStr", alternate = {"methodStr"})
    private String methodStr; // Giá trị mặc định là Voucher (không đổi)

    @SerializedName(value = "Amount", alternate = {"amount"})
    private String amount;  // Giá trị của voucher

    @SerializedName(value = "ID", alternate = {"id"})
    private int id;  // Giá trị mặc định là -1 (không đổi)

    @SerializedName(value = "AccountId", alternate = {"accountId"})
    private Integer accountId; // Giá trị mặc định là null (không đổi)

    @SerializedName(value = "VoucherId", alternate = {"voucherId"})
    private Integer voucherId; // Id của voucher

    @SerializedName(value = "VoucherCampaignId", alternate = {"voucherCampaignId"})
    private Integer voucherCampaignId; // Id của đợt phát hành voucher

}
