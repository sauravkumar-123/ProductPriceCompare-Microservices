package com.pricecompare.ProxyServerFign;

import com.pricecompare.Response.AmazonProductResponse;
import com.pricecompare.Response.FlipcartProductResponse;
import com.pricecompare.Response.ProductManufacturerResponse;

//@FeignClient(name="${product.service.name}",url ="localhost:8080/v1/productapi")
//@FeignClient(name="${product.service.name}")
//@RibbonClient(name="${product.service.name}")
//@FeignClient(name="ProductManufacturer",url ="localhost:8080/v1/productmanufacture")
public interface ProductManufacturerProxy {

//	@GetMapping("/get-all-productdetails")
//	public ProductManufacturerResponse getProductDetailsFromManufacturer(String productCode);
	public ProductManufacturerResponse getProductDetailsFromManufacturer(String productCode);
	public FlipcartProductResponse getProductDetailsFromFlipcart(String productCode);
	public AmazonProductResponse getProductDetailsFromAmazon(String productCode);
}
