package vn.ngong.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.ngong.dto.BannerDto;
import vn.ngong.dto.ProductDto;
import vn.ngong.dto.ProductVariantDto;
import vn.ngong.dto.payment.TransProductDto;
import vn.ngong.entity.Product;
import vn.ngong.entity.ProductVariant;
import vn.ngong.entity.Sale;
import vn.ngong.helper.FormatUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.kiotviet.obj.Attribute;
import vn.ngong.kiotviet.response.DetailProductKiotVietResponse;
import vn.ngong.kiotviet.service.KiotVietService;
import vn.ngong.repository.*;
import vn.ngong.request.ProductFilterRequest;
import vn.ngong.response.ProductFilterDetail;
import vn.ngong.response.ProductFilterResponse;
import vn.ngong.service.ProductService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductVariantRepository productVariantRepository;
	@Autowired
	private ProductNativeRepository productNativeRepository;
	@Autowired
	private SaleRepository saleRepository;
	@Autowired
	private KiotVietService kiotVietService;
	@Autowired
	private CommentNativeRepository commentNativeRepository;
	@Autowired
	private Gson gson;

	@Override
	public ProductDto getProductDetail(int id, DetailProductKiotVietResponse detailProductKiotViet) {
		try {
			Product product = productRepository.findByIdAndStatus(id, 1).orElse(null);
			if (product == null) {
				return null;
			}
			List<ProductVariant> productVariants = productVariantRepository.findAllByProductIdAndStatus(product.getId(), 1);
			List<ProductVariantDto> productVariantDtos = new ArrayList<>();

			for (ProductVariant p : productVariants) {
				Integer quantityStock = getQuantityStockByProductCode(p.getCode());


				ProductVariantDto dto = ProductVariantDto
						.builder()
						.id(p.getId())
						.code(p.getCode())
						.productId(p.getProductId())
						.name(p.getName())
						.price(p.getPrice())
						.salePrice(p.getSalePrice())
						.saleRate(p.getSalePrice() * 100 / p.getPrice())
						.productImages(gson.fromJson(p.getProductImages() == null ? "" : p.getProductImages(), new TypeToken<List<String>>(){}.getType()))
						.variantDetail(gson.fromJson(p.getVariantDetail() == null ? "" : p.getVariantDetail(), Object.class))
						.weight(p.getWeight())
						.quantity(quantityStock == null ? 0 : quantityStock)
						.build();

				productVariantDtos.add(dto);
			}

			return ProductDto.builder()
					.name(product.getName())
					.brandName(product.getBrandName())
					.description(product.getDescription())
					.soGaoFlag(product.getSoGaoFlag())
					.productVariants(productVariantDtos)
					.productImages(gson.fromJson(product.getImage() == null ? "" : product.getImage(), new TypeToken<List<String>>(){}.getType()))
					.attributes(gson.fromJson(product.getAttributes() == null ? "" : product.getAttributes(), new TypeToken<List<Attribute>>(){}.getType()))
					.categoryId(product.getCategoryId())
					.origin(product.getOrigin())
					.nutrition(product.getNutrition())
					.rate(commentNativeRepository.getAvgRate(product.getId()))
					.build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public ProductFilterResponse list(ProductFilterRequest request) {
		try {
			List<ProductFilterDetail> products = productNativeRepository.list(request);
			ProductFilterResponse result = ProductFilterResponse.builder()
					.products(products)
					.pageIndex(request.getPageIndex())
					.pageSize(request.getPageSize())
					.totalItem(0)
					.build();
			result.setCode("00");
			result.setDesc("Success");

			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public ProductFilterResponse getBestSeller(int limit) {
		List<ProductFilterDetail> products = productNativeRepository.findBestSeller(limit);

		ProductFilterResponse result = ProductFilterResponse.builder()
				.products(products)
				.pageIndex(0)
				.pageSize(limit)
				.totalItem(0)
				.build();
		result.setCode("00");
		result.setDesc("Success");

		return result;
	}

	@Override
	public ProductFilterResponse getNewestSale(int limit) {
		List<ProductFilterDetail> products = productNativeRepository.findNewestSale(limit);
		ProductFilterResponse result = ProductFilterResponse.builder()
				.products(products)
				.pageIndex(0)
				.pageSize(limit)
				.totalItem(0)
				.build();
		result.setCode("00");
		result.setDesc("Success");

		return result;
	}

	@Override
	public Product findById(int productId) {
		return productRepository.findById(productId).orElse(null);
	}

	@Override
	public List<String> getBrandNames() {
		return productRepository.getBrandnames();
	}

	@Override
	public List<ProductVariant> findAllProductByCode(List<String> productCodeList) {
//		List<String> productCode = productList.stream().map(p -> p.getProductCode()).collect(Collectors.toList());
		List<ProductVariant> productVariantList = productVariantRepository.findAllByCodeInAndStatus(productCodeList, 1);
		return productVariantList;
	}

	@Override
	public Integer getQuantityStockByProductCode(String productCode) {
		DetailProductKiotVietResponse kiotVietResponse = kiotVietService.getDetailProductByCode(productCode);
		if (kiotVietResponse == null) {
			return null;
		}
		if (kiotVietResponse.getResponseStatus() != null
				&& !ValidtionUtils.checkEmptyOrNull(kiotVietResponse.getResponseStatus().getErrorCode())) {
			return null;
		}
		int totalOnHand = kiotVietResponse.getInventories().stream().mapToInt(i -> i.getOnHand()).sum();

		return totalOnHand;
	}
}
