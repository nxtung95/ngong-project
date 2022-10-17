package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.ngong.dto.ProductDto;
import vn.ngong.entity.Product;
import vn.ngong.helper.FormatUtil;
import vn.ngong.kiotviet.obj.Attribute;
import vn.ngong.kiotviet.response.DetailProductKiotVietResponse;
import vn.ngong.kiotviet.service.GetDetailProductService;
import vn.ngong.repository.ProductRepository;
import vn.ngong.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

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
}
