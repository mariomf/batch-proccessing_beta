package com.mucino.project.etl.service;

import org.springframework.batch.item.ItemProcessor;

import com.mucino.project.etl.domain.Product;

public class ProductItemProcessor implements ItemProcessor<Product, Product> {
	
	@Override
    public Product process(final Product product) throws Exception {
//		String product_name;
//		private String description;
//		private String product_code;
//		private String price;
        final String productName = product.getProduct_name();
        final String description = product.getDescription();
        final String productCode = product.getProduct_code();
        final String price = product.getPrice();

        final Product transformedPerson = new Product(productName, description, productCode, price);

//        log.info("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }

}
