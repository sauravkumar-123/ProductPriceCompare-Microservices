package com.pricecompare.Controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pricecompare.Response.PriceComapreResponse;
import com.pricecompare.Service.PriceCompareServices;

import io.swagger.annotations.Api;

@Api(value = "PriceComapreController", description = "This is PriceComapre Controller for communicate with PriceComaprePortal API")
@RestController
@RequestMapping(value = "/v1/pricecompareapi")
public class PriceComapreController {

	private static final Logger logger = LoggerFactory.getLogger(PriceComapreController.class);

	@Autowired
	private PriceCompareServices priceService;

	@GetMapping("/get-bestPrice")
	public ResponseEntity<PriceComapreResponse> getProductDetails(@RequestParam("productCode") String productCode) {
		Map<String, Object> productSuggested = priceService.getServiceCall(productCode);
		logger.info("<-----Suggested Product ----->" + productSuggested);
		if (null != productSuggested && !productSuggested.isEmpty()) {
			return new ResponseEntity<PriceComapreResponse>(
					new PriceComapreResponse(true, "Suggested Price And Merchant", productSuggested), HttpStatus.OK);
		} else {
			return new ResponseEntity<PriceComapreResponse>(
					new PriceComapreResponse(false, "Unable To Suggest Price And Merchant", productSuggested),
					HttpStatus.NOT_FOUND);
		}
	}
}
