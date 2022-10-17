package vn.ngong.kiotviet.response;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class TokenResponse {
	@SerializedName("access_token")
	private String accessToken;
	@SerializedName("expires_in")
	private String expiresIn;
	@SerializedName("token_type")
	private String tokenType;
	@SerializedName("scope")
	private String scope;
}
