package com.nulvora.nulvora.review.entity;

import com.nulvora.nulvora.common.entity.BaseEntity;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import com.nulvora.nulvora.reviewImg.entity.ReviewImg;
import com.nulvora.nulvora.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "review")
    private List<ReviewImg> reviewImgs = new ArrayList<>();





}
