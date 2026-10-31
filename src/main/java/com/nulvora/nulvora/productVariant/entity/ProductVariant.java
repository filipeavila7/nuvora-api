package com.nulvora.nulvora.productVariant.entity;

import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productImg.entity.ProductImg;
import com.nulvora.nulvora.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private boolean isDefault;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productVariant")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "productVariant")
    private List<ProductImg> productImgs = new ArrayList<>();
}
