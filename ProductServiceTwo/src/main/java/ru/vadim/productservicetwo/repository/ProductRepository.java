package ru.vadim.productservicetwo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vadim.productservicetwo.core.data.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    ProductEntity findByProductId(String productId);
    ProductEntity findByProductIdOrTitle(String productId, String title);
}
