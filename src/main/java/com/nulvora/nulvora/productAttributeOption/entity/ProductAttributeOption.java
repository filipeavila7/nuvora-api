package com.nulvora.nulvora.productAttributeOption.entity;


import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import com.nulvora.nulvora.variantAttribute.entity.VariantAttribute;
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
public class ProductAttributeOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_attribute_id")
    private ProductAttribute productAttribute;


    @OneToMany(mappedBy = "productAttributeOption")
    private List<VariantAttribute> variantAttributes = new ArrayList<>();


}
