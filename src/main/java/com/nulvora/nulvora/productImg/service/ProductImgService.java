package com.nulvora.nulvora.productImg.service;

import com.nulvora.nulvora.productImg.dto.request.ProductImgRequest;
import com.nulvora.nulvora.productImg.entity.ProductImg;
import com.nulvora.nulvora.productImg.repository.ProductImgRepository;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImgService {
    private final ProductImgRepository productImgRepository;

    // criar imagens para a variante, defininindo que apenas uma imagem vai ter primary = true
    public void createVariantImgs(ProductVariant productVariant, List<ProductImgRequest> images){

        // pega todas as fotos em que o primary é true
        long primaryCount = images.stream()
                .filter(ProductImgRequest::isPrimary)
                .count();

        // se tiver mais de uma primary
        if (primaryCount > 1) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Apenas uma imagem pode ser principal"
            );
        }

        if (primaryCount < 1){

        }

        // cria as imagens
        List<ProductImg> productImgs = images.stream()
                .map(img -> {
                    ProductImg productImg = new ProductImg();

                    productImg.setImageUrl(img.imageUrl());
                    productImg.setPrimary(img.isPrimary());
                    productImg.setProductVariant(productVariant);

                    return productImg;
                })
                .toList();

        productImgRepository.saveAll(productImgs);



    }
}
