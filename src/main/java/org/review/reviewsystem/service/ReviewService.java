package org.review.reviewsystem.service;

import org.review.reviewsystem.model.Review;
import org.review.reviewsystem.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviews(){
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(String id){
        return reviewRepository.findById(id);
    }

    public Review saveReview(Review review){
        return reviewRepository.save(review);
    }

    public void deleteReview(String id){
        reviewRepository.deleteById(id);
    }
}
