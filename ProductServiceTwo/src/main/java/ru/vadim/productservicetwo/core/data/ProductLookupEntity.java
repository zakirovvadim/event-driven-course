package ru.vadim.productservicetwo.core.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productlookup")
public class ProductLookupEntity {
    @Id
    private String productId;
    @Column(unique = true)
    private String title;

    public ProductLookupEntity() {
    }

    public ProductLookupEntity(String productId, String title) {
        this.productId = productId;
        this.title = title;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
