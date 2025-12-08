package ru.vadim.productservicetwo.query;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.vadim.productservicetwo.core.data.ProductEntity;
import ru.vadim.productservicetwo.query.rest.ProductRestModel;
import ru.vadim.productservicetwo.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductsQueryHandler {

    private final ProductRepository productRepository;

    public ProductsQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductQuery query) {
        List<ProductRestModel> productRest = new ArrayList<>();
        List<ProductEntity> storedProducts = productRepository.findAll();
        for(ProductEntity productEntity : storedProducts) {
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(productEntity, productRestModel);
            productRest.add(productRestModel);
        }
        return productRest;
    }
}
