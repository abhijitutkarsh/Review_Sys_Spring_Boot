package org.review.reviewsystem.service;

import org.review.reviewsystem.model.Customer;
import org.review.reviewsystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer getVerifiedCustomerById(String id) {
        return customerRepository.findByIdAndIsVerifiedTrue(id);
    }
}
