package com.pricecompare.ProxyServerFign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.pricecompare.ExceptionHandle.GlobalException;
import com.pricecompare.Response.AmazonProductResponse;
import com.pricecompare.Response.FlipcartProductResponse;
import com.pricecompare.Response.ProductManufacturerResponse;

public class ProductManufacturerProxyImpl implements ProductManufacturerProxy {

	private static final Logger logger = LoggerFactory.getLogger(ProductManufacturerProxyImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public ProductManufacturerResponse getProductDetailsFromManufacturer(String productCode) {
		final String URL = "http://ProductManufacturer/v1/productmanufacture/get-productBycode/" + productCode;
		try {
			ProductManufacturerResponse productManufacturerResponse = restTemplate.getForObject(URL, ProductManufacturerResponse.class);
			logger.info("Responce From Manufacturer:{}" + productManufacturerResponse);
			return productManufacturerResponse;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Server Error!!While Calling ProductManufacturer");
		}
	}

	@Override
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

}
