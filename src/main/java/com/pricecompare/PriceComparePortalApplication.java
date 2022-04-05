package com.pricecompare;

import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.pricecompare.Config.RibbonConfiguration;
import com.pricecompare.ProxyServerFign.ProductManufacturerProxy;
import com.pricecompare.ProxyServerFign.ProductManufacturerProxyImpl;

import brave.sampler.Sampler;

@EnableHystrix
@EnableHystrixDashboard
@EnableFeignClients("com.pricecompare")
@EnableDiscoveryClient
@SpringBootApplication
@RibbonClient(name = "{ProductManufacturer,FlipcartProduct,AmazonProduct}", configuration = RibbonConfiguration.class)
public class PriceComparePortalApplication {

	@Value("${server.port}")
	private String portno;

	private static final Logger logger = LoggerFactory.getLogger(PriceComparePortalApplication.class);

//	private static final String dateFormat="dd-MM-yyyy";
	private static final String dateTimeFormat = "dd-MM-yyyy HH:mm:ss";

	public static void main(String[] args) {
		logger.info("<---------PriceComparePortalApplication Project Start------------>");
		SpringApplication.run(PriceComparePortalApplication.class, args);
		logger.info("<---------PriceComparePortalApplication Project End------------>");
	}

	@PostConstruct
	public void init() {
		logger.info("<---------PriceComparePortalApplication Server Run On PortNo:------------>" + portno);
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> {
			builder.simpleDateFormat(dateTimeFormat);
			builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
			builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
			builder.serializers(new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
		};
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

	@Bean
	public ProductManufacturerProxy proxyServer() {
		return new ProductManufacturerProxyImpl();
	}
}
