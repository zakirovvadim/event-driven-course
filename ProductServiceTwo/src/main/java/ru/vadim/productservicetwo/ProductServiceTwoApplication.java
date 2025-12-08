package ru.vadim.productservicetwo;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import ru.vadim.productservicetwo.command.CreateProductCommandInterceptor;
import ru.vadim.productservicetwo.config.XStreamConfig;
import ru.vadim.productservicetwo.core.errorhandling.ProductServiceEventsErrorHandler;

@EnableDiscoveryClient
@SpringBootApplication
@Import(XStreamConfig.class)
public class ProductServiceTwoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceTwoApplication.class, args);
    }

    @Autowired
    public void registerCreateProductCommandInterceptor(ApplicationContext context, CommandBus commandBus) {
        commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
    }

    @Autowired
    public void configurer(EventProcessingConfigurer configurer) {
        configurer.registerListenerInvocationErrorHandler("product-group",
                conf -> new ProductServiceEventsErrorHandler());

//        configurer.registerListenerInvocationErrorHandler("product-group",
//                conf -> PropagatingErrorHandler.instance()); можно заменить по умолчанию хендлером
    }
}
