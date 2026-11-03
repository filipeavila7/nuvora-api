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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_variant_attribute",
                        columnNames = {
                                "product_variant_id",
                                "product_attribute_option_id"
                        }
                )
        }
)
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
