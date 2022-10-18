package vn.ngong.kiotviet.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.config.ShareConfig;
import vn.ngong.kiotviet.response.DetailProductKiotVietResponse;
import vn.ngong.kiotviet.response.TokenResponse;

@Slf4j
@Service
public class KiotVietService {
	@Autowired
	private OkHttpClient okHttpClient;
	@Autowired
	private ShareConfig shareConfig;
	@Autowired
	private Gson gson;

	public String getToken() {
		try {
			RequestBody requestBody = new FormBody.Builder()
					.add("scopes", shareConfig.getKiotVietScopes())
					.add("grant_type", shareConfig.getKiotVietGrantType())
					.add("client_id", shareConfig.getKiotVietClientId())
					.add("client_secret", shareConfig.getKiotVietClientSecret())
					.build();

			Request request = new Request.Builder()
					.url(shareConfig.getKiotVietTokenUrl())
					.post(requestBody)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			if (!response.isSuccessful()) {
				log.info("KiotViet getToken: " + response);
				return null;
			}

			String res = response.body().string();
			log.info("GetToken response: " + res);
			if (StringUtils.isNotBlank(res)) {
				TokenResponse tokenResponse = gson.fromJson(res, TokenResponse.class);
				return tokenResponse.getAccessToken();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public DetailProductKiotVietResponse getDetailProductByCode(String productCode) {
		String accessKey = getToken();
		log.info("Access key: " + accessKey);
		try {
			HttpUrl.Builder urlBuilder = HttpUrl.parse(shareConfig.getKiotVietDetailProductUrl()).newBuilder();
			urlBuilder.addPathSegment(productCode);
			urlBuilder.addQueryParameter("includeSoftDeletedAttribute", "false");
			String url = urlBuilder.build().toString();

			Request request = new Request.Builder()
					.header("Authorization", "Bearer " + accessKey)
					.header("Retailer", shareConfig.getRetailerKiotViet())
					.url(url)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String result = response.body().string();
			log.info("DetailProductKiotVietResponse response: " + result);
			if (StringUtils.isNotBlank(result)) {
				DetailProductKiotVietResponse res = gson.fromJson(result, DetailProductKiotVietResponse.class);
				return res;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
