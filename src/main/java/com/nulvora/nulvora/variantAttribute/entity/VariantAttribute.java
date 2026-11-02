package com.nulvora.nulvora.variantAttribute.entity;


import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VariantAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_attribute_option_id")
    private ProductAttributeOption productAttributeOption;

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;



}
