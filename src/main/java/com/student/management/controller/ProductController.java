package com.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.student.management.Repository.ProductRepository;
import com.student.management.enitity.Products;
import com.student.management.services.ProductService;

import java.util.List;

@RestController

public class ProductController {
    @Autowired
    public ProductRepository repo;

    @GetMapping("/products/")
    public List<Products> getProduct(){
        return repo.findAll();

    }

    @GetMapping("/products/{id}")
    public Products getProducts(@PathVariable Long id){
        return repo.findById(id).orElse(new Products());
    }

  @GetMapping("/products/search-all")
public List<Products> searchAll(
        @RequestParam String q,
        @RequestParam String category) {

    return repo
        .findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(q, category);
}
    @Autowired
private ProductService service;
  @PostMapping("/products/")
public Products add(@RequestBody Products p) {
    return service.save(p);
}
  @PutMapping("/products/{id}")
public Products update(@PathVariable Long id, @RequestBody Products p) {
    return service.update(id, p);
}
@GetMapping("/products/search")
public List<Products> search(@RequestParam String name) {
    return repo.findByNameContainingIgnoreCase(name);
    
}
  @DeleteMapping("/products/{id}")
public void delete(@PathVariable Long id) {
    service.delete(id);
}
    @GetMapping("/products/category")
public List<Products> getByCategory(@RequestParam String name) {
    return repo.findByCategoryIgnoreCase(name);
}
    }

