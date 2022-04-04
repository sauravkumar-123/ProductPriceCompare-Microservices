package com.pricecompare.Service;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface PriceCompareServices {

	public Map<String, Object> getServiceCall(String productCode);

}
