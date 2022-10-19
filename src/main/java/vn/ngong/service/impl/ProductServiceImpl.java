package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.ngong.dto.ProductDto;
import vn.ngong.entity.Product;
import vn.ngong.helper.FormatUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.kiotviet.obj.Attribute;
import vn.ngong.kiotviet.response.DetailProductKiotVietResponse;
import vn.ngong.kiotviet.service.KiotVietService;
import vn.ngong.repository.ProductRepository;
import vn.ngong.request.ProductFilterRequest;
import vn.ngong.response.ProductFilterDetail;
import vn.ngong.response.ProductFilterResponse;
import vn.ngong.service.ProductService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private KiotVietService kiotVietService;

	@Override
	public ProductDto getProductDetail(String code, DetailProductKiotVietResponse detailProductKiotViet) {
		try {
			int totalOnHand = detailProductKiotViet.getInventories().stream().mapToInt(i -> i.getOnHand()).sum();
			Product product = productRepository.findByCodeAndStatus(code, 1).orElse(null);
			if (product == null) {
				return null;
			}

			return ProductDto.builder()
					.name(product.getName())
					.brandName(product.getBrandName())
					.code(code)
					.price(FormatUtil.formatCurrency(product.getPrice()))
					.description(product.getDescription())
					.soGaoFlag(product.getSoGaoFlag())
					.attributeList(detailProductKiotViet.getAttributes())
					.onHand(totalOnHand)
					.build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
    public ProductFilterResponse list(ProductFilterRequest request) {
		try{
			Pageable pageable = PageRequest.of(request.getPageIndex(), request.getPageSize());
			if (request.getMaxPrice() < 0) {
				request.setMinPrice(0);
				request.setMaxPrice(1000000000);
			}

			int order = request.getOrderType();

			List<Product> products = null;
			if (ValidtionUtils.checkEmptyOrNull(request.getBrandName())) {
				if (order == 0) {
					products = productRepository.findAllByCategoryIdAndNameLikeAndPriceIsBetweenAndStatusOrderByPrice(
							request.getCategoryId(),
							"%" + request.getProductName() + "%",
							BigDecimal.valueOf(request.getMinPrice()),
							BigDecimal.valueOf(request.getMaxPrice()),
							1,
							pageable);
				} else {
					products = productRepository.findAllByCategoryIdAndNameLikeAndPriceIsBetweenAndStatusOrderByPriceDesc(
							request.getCategoryId(),
							"%" + request.getProductName() + "%",
							BigDecimal.valueOf(request.getMinPrice()),
							BigDecimal.valueOf(request.getMaxPrice()),
							1,
							pageable);
				}

			} else {
				if (order == 0) {
					products = productRepository.findAllByCategoryIdAndNameLikeAndBrandNameAndPriceIsBetweenAndStatusOrderByPrice(
							request.getCategoryId(),
							"%" + request.getProductName() + "%",
							request.getBrandName(),
							BigDecimal.valueOf(request.getMinPrice()),
							BigDecimal.valueOf(request.getMaxPrice()),
							1,
							pageable);
				} else {
					products = productRepository.findAllByCategoryIdAndNameLikeAndBrandNameAndPriceIsBetweenAndStatusOrderByPriceDesc(
							request.getCategoryId(),
							"%" + request.getProductName() + "%",
							request.getBrandName(),
							BigDecimal.valueOf(request.getMinPrice()),
							BigDecimal.valueOf(request.getMaxPrice()),
							1,
							pageable);
				}
			}

			List<ProductFilterDetail> filterDetails = new ArrayList<ProductFilterDetail>();
			for (Product p : products) {
				ProductFilterDetail item = ProductFilterDetail.builder()
						.id(p.getId())
						.image("")
						.name(p.getName())
						.price(p.getPrice())
						.build();
				filterDetails.add(item);
			}

			ProductFilterResponse result = ProductFilterResponse.builder()
					.products(filterDetails)
					.pageIndex(request.getPageIndex())
					.pageSize(request.getPageSize())
					.totalItem(0)
					.build();
			result.setCode("200");
			result.setDesc("00");

			return result;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public boolean checkInventory(String code) {
		DetailProductKiotVietResponse kiotVietResponse = kiotVietService.getDetailProductByCode(code);
		if (kiotVietResponse == null) {
			return false;
		}
		if (kiotVietResponse.getResponseStatus() != null
				&& !ValidtionUtils.checkEmptyOrNull(kiotVietResponse.getResponseStatus().getErrorCode())) {
			return false;
		}
		int totalOnHand = kiotVietResponse.getInventories().stream().mapToInt(i -> i.getOnHand()).sum();
		return totalOnHand > 0;
	}
}
