package com.mucino.project.etl.configuration;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mucino.project.etl.domain.Product;
import com.mucino.project.etl.service.ProductItemProcessor;
//import com.mucino.project.etl.service.ProductItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer{
	
	
	// tag::readerwriterprocessor[]
    @Bean
    public ItemReader<Product> reader() {
        FlatFileItemReader<Product> reader = new FlatFileItemReader<Product>();
        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<Product>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "product_name", "description", "product_code", "price"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {{
                setTargetType(Product.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ProductItemProcessor processor() {
        return new ProductItemProcessor();
    }

    @Bean
    public ItemWriter<Product> writer() {//Product == Record
        MongoItemWriter<Product> writer = new MongoItemWriter<Product>();
        try {
            writer.setTemplate(mongoTemplate());
        } catch (Exception e) {
            //log.error(e.toString());
        }
        writer.setCollection("products");
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step s1) {//JobExecutionListener listener
        return jobs.get("importUserJob")
                .incrementer(new RunIdIncrementer())
//                .listener(listener)
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Product> reader,
            ItemWriter<Product> writer, ItemProcessor<Product, Product> processor) {
        return stepBuilderFactory.get("step1")
                .<Product, Product> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    // end::jobstep[]


    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
//        return new SimpleMongoDbFactory(new MongoClient(), "db-name");
        return new SimpleMongoDbFactory(new MongoClient(), "STOCK_CONTROL_DB");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

}
