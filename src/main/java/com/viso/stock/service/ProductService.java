package com.viso.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.viso.stock.exceptions.NotFoundException;
import com.viso.stock.model.ProductEntity;
import com.viso.stock.repository.ProductRepository;

@Service
public class ProductService {
    private final String source = "Product";
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long createProduct(ProductEntity product) {
        return productRepository.save(product).getId();
    }

    public List<ProductEntity> findAll() {
        return productRepository.findAll();
    }

    public ProductEntity findByBarcode(String barcode) {
        final ProductEntity product = productRepository.findByBarcode(barcode);
        if (product == null) {
            throw new NotFoundException("Product not found with barcode " + barcode, source);
        }
        return product;
    }

    public ProductEntity updateProduct(Long id, ProductEntity product) {
        return productRepository.findById(id).map(existingProduct -> {
            if(existingProduct.getBarcode() == null ? product.getBarcode() != null : !existingProduct.getBarcode().equals(product.getBarcode())) {
                throw new RuntimeException("Cannot change barcode of existing product");
            }
            existingProduct.setName(product.getName() != null ? product.getName() : existingProduct.getName());
            existingProduct.setBuyPrice(product.getBuyPrice() != 0 ? product.getBuyPrice() : existingProduct.getBuyPrice());
            existingProduct.setSellPrice(product.getSellPrice() != 0 ? product.getSellPrice() : existingProduct.getSellPrice());
            existingProduct.setStockQuantity(product.getStockQuantity() != 0 ? product.getStockQuantity() : existingProduct.getStockQuantity());
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new NotFoundException("Product not found with id " + id, source));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
