package vn.ngong.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ShareConfig {
	@Value("${kiotviet.token.url:https://id.kiotviet.vn/connect/token}")
	private String kiotVietTokenUrl;

	@Value("${kiotviet.token.scopes:PublicApi.Access}")
	private String kiotVietScopes;

	@Value("${kiotviet.token.grant-type:client_credentials}")
	private String kiotVietGrantType;

	@Value("${kiotviet.token.client-id:b0c70023-7e2d-452b-a06a-dd9820931964}")
	private String kiotVietClientId;

	@Value("${kiotviet.token.client-secret:41F4E5A10F57D466F6614A4A584F83D1FC6B533B}")
	private String kiotVietClientSecret;

	@Value("${kiotviet.token.detail-product.url:https://public.kiotapi.com/products/code}")
	private String kiotVietDetailProductUrl;

	@Value("${kiotviet.token.order.url:https://public.kiotapi.com/orders}")
	private String orderUrl;

	@Value("${kiotviet.token.get.order.url:https://public.kiotapi.com/orders/code}")
	private String getOrderUrl;

	@Value("${kiotviet.retailer:rausachviet1808}")
	private String retailerKiotViet;

	@Value("${jwt-token-validity-time:18000}")
	private Integer validityTokenTime;

	@Value("{amount.fix.remain.gao=54000}")
	private String amountFixRemainGao;
}
