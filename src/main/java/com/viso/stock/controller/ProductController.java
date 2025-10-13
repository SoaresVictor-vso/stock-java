package com.viso.stock.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viso.stock.model.ProductEntity;
import com.viso.stock.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Long createProduct(@RequestBody ProductEntity product) {
        return productService.createProduct(product);
    }

    @GetMapping
    public List<ProductEntity> getProducts() {
        return productService.findAll();
    }

    @GetMapping("/barcode/{barcode}")
    public ProductEntity getProductByBarcode(@PathVariable String barcode) {
        System.out.println("Searching for product with barcode: " + barcode);
        return productService.findByBarcode(barcode);
    }

    @PutMapping("/{id}")
    public ProductEntity updateProduct(@PathVariable Long id, @RequestBody ProductEntity product) {
        System.out.println("Updating product with id: " + id);
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        System.out.println("Deleting product with id: " + id);
        productService.deleteProduct(id);
    }
}
