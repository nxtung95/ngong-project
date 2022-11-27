package vn.ngong.kiotviet.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.config.ShareConfig;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.kiotviet.request.CreateCustomerRequest;
import vn.ngong.kiotviet.request.CreateOrdersRequest;
import vn.ngong.kiotviet.request.GetOrderKiotVietRequest;
import vn.ngong.kiotviet.response.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KiotVietService {
	@Autowired
	private OkHttpClient okHttpClient;
	@Autowired
	private ShareConfig shareConfig;
	@Autowired
	private Gson gson;

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static String accessToken;

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
				this.accessToken = tokenResponse.getAccessToken();
				return tokenResponse.getAccessToken();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public DetailProductKiotVietResponse getDetailProductByCode(String productCode) {
		if (ValidtionUtils.checkEmptyOrNull(this.accessToken)) {
			getToken();
		}
//		log.info("Access key: " + this.accessToken);
		try {
			HttpUrl.Builder urlBuilder = HttpUrl.parse(shareConfig.getKiotVietDetailProductUrl()).newBuilder();
			urlBuilder.addPathSegment(productCode);
			urlBuilder.addQueryParameter("includeSoftDeletedAttribute", "false");
			String url = urlBuilder.build().toString();

			Request request = new Request.Builder()
					.header("Authorization", "Bearer " + this.accessToken)
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

	public CreateOrdersResponse createOrders(CreateOrdersRequest rq) {
		if (ValidtionUtils.checkEmptyOrNull(this.accessToken)) {
			getToken();
		}
		log.info("createOrders@request" + gson.toJson(rq));
		try {
			HttpUrl.Builder urlBuilder = HttpUrl.parse(shareConfig.getOrderUrl()).newBuilder();
			String url = urlBuilder.build().toString();
			RequestBody body = RequestBody.create(JSON, gson.toJson(rq));

			Request request = new Request.Builder()
					.header("Authorization", "Bearer " + this.accessToken)
					.header("Retailer", shareConfig.getRetailerKiotViet())
					.url(url)
					.post(body)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String result = response.body().string();
			log.info("CreateOrders response: " + result);
			if (StringUtils.isNotBlank(result)) {
				CreateOrdersResponse res = gson.fromJson(result, CreateOrdersResponse.class);
				return res;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public CreateCustomerResponse createCustomer(CreateCustomerRequest rq) {
		if (ValidtionUtils.checkEmptyOrNull(this.accessToken)) {
			getToken();
		}
		log.info("createCustomer@request" + gson.toJson(rq));
		try {
			HttpUrl.Builder urlBuilder = HttpUrl.parse(shareConfig.getCustomerUrl()).newBuilder();
			String url = urlBuilder.build().toString();
			RequestBody body = RequestBody.create(JSON, gson.toJson(rq));

			Request request = new Request.Builder()
					.header("Authorization", "Bearer " + this.accessToken)
					.header("Retailer", shareConfig.getRetailerKiotViet())
					.url(url)
					.post(body)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String result = response.body().string();
			log.info("createCustomer response: " + result);
			if (StringUtils.isNotBlank(result)) {
				CreateCustomerResponse res = gson.fromJson(result, CreateCustomerResponse.class);
				return res;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public GetOrderKiotVietResponse getOrderByCustomerCode(GetOrderKiotVietRequest rq) {
		if (ValidtionUtils.checkEmptyOrNull(this.accessToken)) {
			getToken();
		}
		log.info("GetOrder@request" + gson.toJson(rq));
		try {
			HttpUrl.Builder urlBuilder = HttpUrl.parse(shareConfig.getOrderUrl()).newBuilder();
			urlBuilder.addQueryParameter("customerCode", rq.getCustomerCode());
			urlBuilder.addQueryParameter("includeOrderDelivery", String.valueOf(rq.isIncludeOrderDelivery()));
			urlBuilder.addQueryParameter("includePayment", String.valueOf(rq.isIncludePayment()));
			urlBuilder.addQueryParameter("orderBy", rq.getOrderBy());
			urlBuilder.addQueryParameter("orderDirection", rq.getOrderDirection());
			if (rq.getStatus() != null) {
				String status = Arrays.stream(rq.getStatus()).map(s -> String.valueOf(s)).collect(Collectors.joining(","));
				urlBuilder.addQueryParameter("status", "[" + status + "]");
			}
			String url = urlBuilder.build().toString();
			log.info("GetOrder@url" + url);
			Request request = new Request.Builder()
					.header("Authorization", "Bearer " + this.accessToken)
					.header("Retailer", shareConfig.getRetailerKiotViet())
					.url(url)
					.get()
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String result = response.body().string();
			log.info("GetOrderResponse: " + result);
			if (StringUtils.isNotBlank(result)) {
				GetOrderKiotVietResponse res = gson.fromJson(result, GetOrderKiotVietResponse.class);
				return res;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public DataCustomerResponse getDetailCus(String code) {
		if (ValidtionUtils.checkEmptyOrNull(this.accessToken)) {
			getToken();
		}
		try {
			HttpUrl.Builder urlBuilder = HttpUrl.parse(shareConfig.getCustomerUrl() + "/code").newBuilder();
			urlBuilder.addPathSegment(code);
			String url = urlBuilder.build().toString();

			Request request = new Request.Builder()
					.header("Authorization", "Bearer " + this.accessToken)
					.header("Retailer", shareConfig.getRetailerKiotViet())
					.url(url)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String result = response.body().string();
			log.info("getDetailCus@response: " + result);
			if (StringUtils.isNotBlank(result)) {
				DataCustomerResponse res = gson.fromJson(result, DataCustomerResponse.class);
				return res;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
