package com.pricecompare.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pricecompare.ExceptionHandle.GlobalException;
import com.pricecompare.ProxyServerFign.ProductManufacturerProxy;
import com.pricecompare.Response.AmazonProductResponse;
import com.pricecompare.Response.FlipcartProductResponse;
import com.pricecompare.Response.ProductManufacturerResponse;
import com.pricecompare.Service.PriceCompareServices;

@Service
public class PriceCompareServicesImpl implements PriceCompareServices {

	private static final Logger logger = LoggerFactory.getLogger(PriceCompareServicesImpl.class);

	@Autowired
	private ProductManufacturerProxy proxyServers;

	@Override
	public Map<String, Object> getServiceCall(String productCode) {
		ProductManufacturerResponse productManufacturerResponse = proxyServers
				.getProductDetailsFromManufacturer(productCode);
		FlipcartProductResponse flipcartProductResponse = proxyServers.getProductDetailsFromFlipcart(productCode);
		AmazonProductResponse amazonProductResponse = proxyServers.getProductDetailsFromAmazon(productCode);
		logger.info("Responce From Manufacturer:{}" + productManufacturerResponse);
		logger.info("Responce From Flipcart:{}" + flipcartProductResponse);
		logger.info("Responce From Amazon:{}" + amazonProductResponse);
		if (null != productManufacturerResponse && productManufacturerResponse.isStatus()) {
			if (null != flipcartProductResponse && flipcartProductResponse.isStatus()) {
				if (null != amazonProductResponse && amazonProductResponse.isStatus()) {
					Map<String, Object> map = new HashMap<>();
					try {
						@SuppressWarnings("unchecked")
						Map<String, Object> manufacturerProduct = (Map<String, Object>) productManufacturerResponse
								.getDatasource();
						manufacturerProduct.entrySet().stream().forEach(p -> {
							if (p.getKey().equalsIgnoreCase("productCode")) {
								map.put("manufacturerProductCode", p.getValue());
							}
							if (p.getKey().equalsIgnoreCase("productName")) {
								map.put("manufacturerProductName", p.getValue());
							}
							if (p.getKey().equalsIgnoreCase("productprice")) {
								map.put("manufacturerProductPrice", p.getValue());
							}
						});

						@SuppressWarnings("unchecked")
						Map<String, Object> flipcartProduct = (Map<String, Object>) flipcartProductResponse
								.getDatasource();
						flipcartProduct.entrySet().stream().forEach(f -> {
							if (f.getKey().equalsIgnoreCase("productCode")) {
								map.put("flipcartProductCode", f.getValue());
							}
							if (f.getKey().equalsIgnoreCase("productName")) {
								map.put("flipcartProductName", f.getValue());
							}
							if (f.getKey().equalsIgnoreCase("flipcartProductprice")) {
								map.put("flipcartProductPrice", f.getValue());
							}
						});

						@SuppressWarnings("unchecked")
						Map<String, Object> amazonProduct = (Map<String, Object>) amazonProductResponse.getDatasource();
						amazonProduct.entrySet().stream().forEach(a -> {
							if (a.getKey().equalsIgnoreCase("productCode")) {
								map.put("amazonProductCode", a.getValue());
							}
							if (a.getKey().equalsIgnoreCase("productName")) {
								map.put("amazonProductName", a.getValue());
							}
							if (a.getKey().equalsIgnoreCase("amazonProductprice")) {
								map.put("amazonProductPrice", a.getValue());
							}
						});
						logger.info("Product Details From Each Portal's{} " + map);
						return getBestPrice(map);
					} catch (Exception ex) {
						throw new GlobalException(ex.getMessage());
					}
				} else {
					throw new NullPointerException("Invalid Response From AmazonPortal");
				}
			} else {
				throw new NullPointerException("Invalid Response From FlipcartPortal");
			}
		} else {
			throw new NullPointerException("Invalid Response From ProductManufacturer");
		}
	}

	private Map<String, Object> getBestPrice(Map<String, Object> input) {
		logger.info("Product Details From Each Portal's{} " + input);
		String productName;
		Double maufacturerPrice;
		Double flipcartPrice;
		Double amazonPrice;
		Map<String, Object> map = new HashMap<>();
		try {
			productName = Optional.of(input.get("manufacturerProductName").toString()).orElseThrow(Exception::new);
			maufacturerPrice = Optional.of(Double.parseDouble(input.get("manufacturerProductPrice").toString()))
					.orElseThrow(Exception::new);
			flipcartPrice = Optional.of(Double.parseDouble(input.get("flipcartProductPrice").toString()))
					.orElseThrow(Exception::new);
			amazonPrice = Optional.of(Double.parseDouble(input.get("amazonProductPrice").toString()))
					.orElseThrow(Exception::new);

			if (flipcartPrice < maufacturerPrice && flipcartPrice < amazonPrice) {
				map.put("productName", productName);
				map.put("wholeSalePrice", maufacturerPrice);
				map.put("amazonSalePrice", amazonPrice);
				map.put("flipcartSalePrice", flipcartPrice);
				map.put("suggestion", "You Should Buy From Flipcart Only!!");
			} else if (amazonPrice < maufacturerPrice && amazonPrice < flipcartPrice) {
				map.put("productName", productName);
				map.put("wholeSalePrice", maufacturerPrice);
				map.put("amazonSalePrice", amazonPrice);
				map.put("flipcartSalePrice", flipcartPrice);
				map.put("suggestion", "You Should Buy From Amazon Only!!");
			} else {
				map.put("productName", productName);
				map.put("wholeSalePrice", maufacturerPrice);
				map.put("amazonSalePrice", amazonPrice);
				map.put("flipcartSalePrice", flipcartPrice);
				map.put("suggestion", "You Can Buy From Anyone!!");
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Error While Fetching ProductDetails!!!Try After Some Time");
		}

	}

}
