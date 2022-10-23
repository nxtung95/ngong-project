package vn.ngong.kiotviet.request;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class TokenRequest {
	@SerializedName("scopes")
	private String scopes;
	@SerializedName("grant_type")
	private String grantType;
	@SerializedName("client_id")
	private String clientId;
	@SerializedName("client_secret")
	private String clientSecret;
}
