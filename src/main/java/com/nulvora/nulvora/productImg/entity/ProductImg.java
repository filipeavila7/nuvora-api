package com.nulvora.nulvora.productImg.entity;


import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductVariant productVariant;

    private String imageUrl;

    private Boolean isPrimary = false;
}
