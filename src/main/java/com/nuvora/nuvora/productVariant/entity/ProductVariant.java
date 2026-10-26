package com.nuvora.nuvora.productVariant.entity;

import com.nuvora.nuvora.product.entity.Product;
import com.nuvora.nuvora.review.entity.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;

    private String color;

    private String size;

    private BigDecimal price;

    private Integer stockQuantity;

    private boolean isDefault;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productVariant")
    private List<Review> reviews = new ArrayList<>();
}
