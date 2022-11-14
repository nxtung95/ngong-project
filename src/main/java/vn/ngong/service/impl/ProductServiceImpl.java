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
import vn.ngong.entity.Sale;
import vn.ngong.helper.FormatUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.kiotviet.obj.Attribute;
import vn.ngong.kiotviet.response.DetailProductKiotVietResponse;
import vn.ngong.kiotviet.service.KiotVietService;
import vn.ngong.repository.ProductNativeRepository;
import vn.ngong.repository.ProductRepository;
import vn.ngong.repository.SaleRepository;
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
	private ProductNativeRepository productNativeRepository;
	@Autowired
	private SaleRepository saleRepository;
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
			// Lấy tất cả sản phẩm, không theo danh mục
			if (request.getCategoryId() <= 0) {
				if (ValidtionUtils.checkEmptyOrNull(request.getBrandName())) {
					if (order == 0) {
						products = productRepository.findAllByNameLikeAndPriceIsBetweenAndStatusOrderByPrice(
								"%" + request.getProductName() + "%",
								request.getMinPrice(),
								request.getMaxPrice(),
								1,
								pageable);
					} else {
						products = productRepository.findAllByNameLikeAndPriceIsBetweenAndStatusOrderByPriceDesc(
								"%" + request.getProductName() + "%",
								request.getMinPrice(),
								request.getMaxPrice(),
								1,
								pageable);
					}

				} else {
					if (order == 0) {
						products = productRepository.findAllByNameLikeAndBrandNameAndPriceIsBetweenAndStatusOrderByPrice(
								"%" + request.getProductName() + "%",
								request.getBrandName(),
								request.getMinPrice(),
								request.getMaxPrice(),
								1,
								pageable);
					} else {
						products = productRepository.findAllByNameLikeAndBrandNameAndPriceIsBetweenAndStatusOrderByPriceDesc(
								"%" + request.getProductName() + "%",
								request.getBrandName(),
								request.getMinPrice(),
								request.getMaxPrice(),
								1,
								pageable);
					}
				}
			} else { // Lấy sản phẩm theo danh mục
				if (ValidtionUtils.checkEmptyOrNull(request.getBrandName())) {
					if (order == 0) {
						products = productRepository.findAllByCategoryIdAndNameLikeAndPriceIsBetweenAndStatusOrderByPrice(
								request.getCategoryId(),
								"%" + request.getProductName() + "%",
								request.getMinPrice(),
								request.getMaxPrice(),
								1,
								pageable);
					} else {
						products = productRepository.findAllByCategoryIdAndNameLikeAndPriceIsBetweenAndStatusOrderByPriceDesc(
								request.getCategoryId(),
								"%" + request.getProductName() + "%",
								request.getMinPrice(),
								request.getMaxPrice(),
								1,
								pageable);
					}
				} else {
					if (order == 0) {
						products = productRepository.findAllByCategoryIdAndNameLikeAndBrandNameAndPriceIsBetweenAndStatusOrderByPrice(
								request.getCategoryId(),
								"%" + request.getProductName() + "%",
								request.getBrandName(),
								request.getMinPrice(),
								request.getMaxPrice(),
								1,
								pageable);
					} else {
						products = productRepository.findAllByCategoryIdAndNameLikeAndBrandNameAndPriceIsBetweenAndStatusOrderByPriceDesc(
								request.getCategoryId(),
								"%" + request.getProductName() + "%",
								request.getBrandName(),
								request.getMinPrice(),
								request.getMaxPrice(),
								1,
								pageable);
					}
				}
			}

			List<ProductFilterDetail> filterDetails = new ArrayList<ProductFilterDetail>();
			for (Product p : products) {
				Sale sale = saleRepository.findFirstByProductIdAndStatusOrderByUpdatedAtDesc(p.getId(), 1);
				if (sale == null) sale = new Sale();
				ProductFilterDetail item = ProductFilterDetail.builder()
						.id(p.getId())
						.image("")
						.name(p.getName())
						.code(p.getCode())
						.price(p.getPrice())
						.saleName(sale.getName())
						.saleRate((int)(sale.getRate() * 100))
						.salePrice(sale.getSalePrice())
						.saleStartTime(sale.getStartTime())
						.saleEndTime(sale.getEndTime())
						.build();
				filterDetails.add(item);
			}

			ProductFilterResponse result = ProductFilterResponse.builder()
					.products(filterDetails)
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
