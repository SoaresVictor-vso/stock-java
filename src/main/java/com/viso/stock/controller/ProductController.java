package com.viso.stock.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('product:write')")
    public Long createProduct(@RequestBody ProductEntity product) {
        return productService.createProduct(product);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('product:read')")
    public List<ProductEntity> getProducts() {
        return productService.findAll();
    }

    @GetMapping("/barcode/{barcode}")
    @PreAuthorize("hasAuthority('product:read')")
    public ProductEntity getProductByBarcode(@PathVariable String barcode) {
        System.out.println("Searching for product with barcode: " + barcode);
        return productService.findByBarcode(barcode);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product:update')")
    public ProductEntity updateProduct(@PathVariable Long id, @RequestBody ProductEntity product) {
        System.out.println("Updating product with id: " + id);
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product:delete')")
    public void deleteProduct(@PathVariable Long id) {
        System.out.println("Deleting product with id: " + id);
        productService.deleteProduct(id);
    }

    /**
     * Endpoint that demonstrates programmatic permission checking in service layer
     */
    @GetMapping("/with-permission-check")
    public List<ProductEntity> getProductsWithPermissionCheck() {
        return productService.findAllWithPermissionCheck();
    }

    /**
     * Enhanced update endpoint that uses advanced permission checking
     */
    @PutMapping("/advanced/{id}")
    public ProductEntity updateProductAdvanced(@PathVariable Long id, @RequestBody ProductEntity product) {
        System.out.println("Updating product with advanced permissions for id: " + id);
        return productService.updateProductWithAdvancedPermissions(id, product);
    }
}
