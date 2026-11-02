package com.nulvora.nulvora.productAttribute.entity;


import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productAttribute")
    private List<ProductAttributeOption> options = new ArrayList<>();

}
