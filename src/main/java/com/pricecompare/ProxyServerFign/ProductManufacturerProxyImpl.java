package com.pricecompare.ProxyServerFign;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.pricecompare.ExceptionHandle.GlobalException;
import com.pricecompare.Response.AmazonProductResponse;
import com.pricecompare.Response.FlipcartProductResponse;
import com.pricecompare.Response.ProductManufacturerResponse;

public class ProductManufacturerProxyImpl implements ProductManufacturerProxy {

	private static final Logger logger = LoggerFactory.getLogger(ProductManufacturerProxyImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Override
	@HystrixCommand(commandKey = "ProductManufacturer", groupKey = "ProductManufacturer", fallbackMethod = "getProductDetailsFromManufacturerFallback")
	public ProductManufacturerResponse getProductDetailsFromManufacturer(String productCode) {
		final String URL = "http://ProductManufacturer/v1/productmanufacture/get-productBycode/" + productCode;
		try {
			ProductManufacturerResponse productManufacturerResponse = restTemplate.getForObject(URL,
					ProductManufacturerResponse.class);
			logger.info("Responce From Manufacturer:{}" + productManufacturerResponse);
			return productManufacturerResponse;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Server Error!!While Calling ProductManufacturer");
		}
	}

	@Override
	@HystrixCommand(commandKey = "FlipcartProduct", groupKey = "FlipcartProduct", fallbackMethod = "getProductDetailsFromFlipcartProductFallback")
	public FlipcartProductResponse getProductDetailsFromFlipcart(String productCode) {
		final String URL = "http://FlipcartProduct/v1/flipcartproductapi/get-productBycode/" + productCode;
		try {
			FlipcartProductResponse flipcartResponse = restTemplate.getForObject(URL, FlipcartProductResponse.class);
			logger.info("Responce From Flipcart:{}" + flipcartResponse);
			return flipcartResponse;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Server Error!!While Calling Filpcart Product");
		}
	}

	@Override
	@HystrixCommand(commandKey = "AmazonProduct", groupKey = "AmazonProduct", fallbackMethod = "getProductDetailsFromAmazonProductProductFallback")
	public AmazonProductResponse getProductDetailsFromAmazon(String productCode) {
		final String URL = "http://AmazonProduct/v1/amazonproductapi/get-productBycode/" + productCode;
		try {
			AmazonProductResponse amazonResponse = restTemplate.getForObject(URL, AmazonProductResponse.class);
			logger.info("Responce From Amazon:{}" + amazonResponse);
			return amazonResponse;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Server Error!!While Calling Amazon Product");
		}
	}

	public ProductManufacturerResponse getProductDetailsFromManufacturerFallback(String productCode) {
		ProductManufacturerResponse maufacturerResponse = new ProductManufacturerResponse(ZonedDateTime.now(), false,
				"ProductManufacturer Service Gateway Not Respond!!Try Again After Some Time", null);
		return maufacturerResponse;
	}

	public FlipcartProductResponse getProductDetailsFromFlipcartProductFallback(String productCode) {
		FlipcartProductResponse flipcartResponse = new FlipcartProductResponse(ZonedDateTime.now(), false,
				"FlipcartProduct Service Gateway Not Respond!!Try Again After Some Time", null);
		return flipcartResponse;
	}

	public AmazonProductResponse getProductDetailsFromAmazonProductProductFallback(String productCode) {
		AmazonProductResponse amazonResponse = new AmazonProductResponse(ZonedDateTime.now(), false,
				"AmazonProduct Service Gateway Not Respond!!Try Again After Some Time", null);
		return amazonResponse;
	}
}
