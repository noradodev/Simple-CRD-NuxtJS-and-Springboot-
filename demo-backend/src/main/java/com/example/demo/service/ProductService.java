package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public List<Product> getAll(){
        return productRepository.findAll();
    }
    public Optional<Product> getProductById(int id){
        return productRepository.findById(id);
    }
    public Product addProduct(Product product){
        return productRepository.save(product);
    }
    public Optional<Product> updateProduct(Product updatedProduct, int id){
        return productRepository.findById(id).map((product) -> {
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            return productRepository.save(product);
        });
    }

    public Boolean deleteProduct(int id){
        return productRepository.findById(id).map(product -> {
            productRepository.delete(product);
            return true;
        }).orElse(false);
    }
    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainsIgnoreCase(name);
    }
}
