package org.review.reviewsystem.service;

import org.review.reviewsystem.model.Product;
import org.review.reviewsystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product getProductById(String id) {
        Optional<Product> productOpt = productRepository.findById(id);
        return productOpt.orElse(null);
    }
}
