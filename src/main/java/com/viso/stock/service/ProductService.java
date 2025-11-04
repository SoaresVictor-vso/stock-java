package com.viso.stock.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.viso.stock.exceptions.NotFoundException;
import com.viso.stock.model.Permission;
import com.viso.stock.model.ProductEntity;
import com.viso.stock.repository.ProductRepository;

@Service
public class ProductService {
    private final String source = "Product";
    private final ProductRepository productRepository;
    private final PermissionService permissionService;

    public ProductService(ProductRepository productRepository, PermissionService permissionService) {
        this.productRepository = productRepository;
        this.permissionService = permissionService;
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

    /**
     * Example method that demonstrates programmatic permission checking
     * This method checks permissions within the service layer rather than using annotations
     */
    public List<ProductEntity> findAllWithPermissionCheck() {
        // Check if user has permission to read products
        if (!permissionService.hasPermission(Permission.PRODUCT_READ)) {
            throw new AccessDeniedException("User does not have permission to read products");
        }
        
        return productRepository.findAll();
    }

    /**
     * Enhanced update method that checks for different permission levels
     */
    public ProductEntity updateProductWithAdvancedPermissions(Long id, ProductEntity product) {
        // Check basic update permission
        if (!permissionService.hasPermission(Permission.PRODUCT_UPDATE)) {
            throw new AccessDeniedException("User does not have permission to update products");
        }

        return productRepository.findById(id).map(existingProduct -> {
            // Check if trying to change sensitive fields that might require higher permissions
            if (product.getBuyPrice() != 0 && product.getBuyPrice() != existingProduct.getBuyPrice()) {
                // For demonstration: require both update and write permissions for price changes
                if (!permissionService.hasAllPermissions(Permission.PRODUCT_UPDATE, Permission.PRODUCT_WRITE)) {
                    throw new AccessDeniedException("User does not have sufficient permissions to change product prices");
                }
            }

            // Proceed with update
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
}
