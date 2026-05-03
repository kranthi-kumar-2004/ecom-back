package com.student.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.management.Repository.ProductRepository;
import com.student.management.enitity.Products;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<Products> getAll() {
        return repo.findAll();
    }

    public Products save(Products p) {
        applyDiscount(p);
        return repo.save(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Products update(Long id, Products p) {

        Products existing = repo.findById(id).orElse(null);

        if (existing == null) {
            System.out.println("ID NOT FOUND: " + id);
            return null;
        }

        existing.setName(p.getName());
        existing.setPrice(p.getPrice());
        existing.setImage(p.getImage());
        existing.setCategory(p.getCategory());
        existing.setStock(p.getStock());
        existing.setDescription(p.getDescription());

        /* ✅ update discount */
        existing.setDiscount_percent(p.getDiscount_percent());

        applyDiscount(existing);

        return repo.save(existing);
    }

    /* 🔥 COMMON METHOD */
    private void applyDiscount(Products p) {

        double price = p.getPrice();

        double discount = p.getDiscount_percent() != null 
        ? p.getDiscount_percent() 
        : 0;

        /* ✅ validation */
        if (discount < 0) discount = 0;
        if (discount > 100) discount = 100;

        double finalPrice = price - (price * discount / 100);

        p.setDiscount_percent(discount);
        p.setDiscount_price(finalPrice);
    }
}