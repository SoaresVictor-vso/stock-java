# Permission Testing System

This application now includes a comprehensive permission-based authentication and authorization system. Here's how to use and test the current user role permissions.

## Overview

The system uses JWT tokens for authentication and role-based permissions for authorization. Each user has roles, and each role has specific permissions that control access to different resources and operations.

## Available Permissions

The system defines the following permissions:

### User Permissions
- `user:read` - Read user information
- `user:write` - Create new users
- `user:update` - Update existing users
- `user:delete` - Delete users

### Product Permissions
- `product:read` - Read product information
- `product:write` - Create new products
- `product:update` - Update existing products
- `product:delete` - Delete products

## Authentication Flow

1. **Login**: Send POST request to `/auth/login` with credentials
2. **Receive JWT**: Get JWT token in response
3. **Use JWT**: Include JWT in `Authorization: Bearer <token>` header for subsequent requests

## Testing Permission System

### 1. Authentication Testing

**Test Public Endpoint (No Authentication Required):**
```bash
curl -X GET http://localhost:8080/test/public
```

**Test Authentication Status:**
```bash
curl -X GET http://localhost:8080/test/auth \
  -H "Authorization: Bearer <your-jwt-token>"
```

### 2. Current User Information

**Get Current User Details and Permissions:**
```bash
curl -X GET http://localhost:8080/permissions/current-user \
  -H "Authorization: Bearer <your-jwt-token>"
```

This endpoint returns:
- User authentication status
- User ID and name
- User roles
- User permissions

### 3. Permission Testing Endpoints

**Test Specific Permission:**
```bash
curl -X GET "http://localhost:8080/permissions/test?permission=product:read" \
  -H "Authorization: Bearer <your-jwt-token>"
```

**Test Multiple Permissions:**
```bash
curl -X GET "http://localhost:8080/permissions/test-multiple?permissions=user:read,product:write,user:delete" \
  -H "Authorization: Bearer <your-jwt-token>"
```

**Check Resource Access:**
```bash
curl -X GET http://localhost:8080/permissions/resource-access \
  -H "Authorization: Bearer <your-jwt-token>"
```

**Get Available Permissions:**
```bash
curl -X GET http://localhost:8080/permissions/available \
  -H "Authorization: Bearer <your-jwt-token>"
```

### 4. Protected Endpoint Testing

**User Endpoints (Require user permissions):**
```bash
# Read users (requires user:read)
curl -X GET http://localhost:8080/users \
  -H "Authorization: Bearer <your-jwt-token>"

# Create user (requires user:write)
curl -X POST http://localhost:8080/users \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"password"}'

# Get specific user (requires user:read)
curl -X GET http://localhost:8080/users/{user-id} \
  -H "Authorization: Bearer <your-jwt-token>"
```

**Product Endpoints (Require product permissions):**
```bash
# Read products (requires product:read)
curl -X GET http://localhost:8080/products \
  -H "Authorization: Bearer <your-jwt-token>"

# Create product (requires product:write)
curl -X POST http://localhost:8080/products \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Product","barcode":"123456789","buyPrice":10.0,"sellPrice":15.0,"stockQuantity":100}'

# Update product (requires product:update)
curl -X PUT http://localhost:8080/products/{product-id} \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Product","buyPrice":12.0,"sellPrice":18.0}'

# Delete product (requires product:delete)
curl -X DELETE http://localhost:8080/products/{product-id} \
  -H "Authorization: Bearer <your-jwt-token>"
```

### 5. Advanced Permission Testing

**Programmatic Permission Check (Service Layer):**
```bash
# This endpoint demonstrates permission checking within the service layer
curl -X GET http://localhost:8080/products/with-permission-check \
  -H "Authorization: Bearer <your-jwt-token>"
```

**Advanced Update with Complex Permission Logic:**
```bash
# This endpoint requires both product:update and product:write for price changes
curl -X PUT http://localhost:8080/products/advanced/{product-id} \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{"buyPrice":20.0,"sellPrice":30.0}'
```

## Implementation Details

### 1. Annotation-Based Security
Controllers use `@PreAuthorize` annotations to declare required permissions:
```java
@PreAuthorize("hasAuthority('product:read')")
public List<ProductEntity> getProducts() { ... }
```

### 2. Programmatic Permission Checking
Services can programmatically check permissions using `PermissionService`:
```java
if (!permissionService.hasPermission(Permission.PRODUCT_READ)) {
    throw new AccessDeniedException("User does not have permission to read products");
}
```

### 3. Permission Service Methods
The `PermissionService` provides several utility methods:
- `getCurrentUser()` - Get current authenticated user
- `hasPermission(Permission)` - Check single permission
- `hasAnyPermission(Permission...)` - Check if user has any of the permissions
- `hasAllPermissions(Permission...)` - Check if user has all permissions
- `getCurrentUserPermissions()` - Get all user permissions
- `getCurrentUserRoles()` - Get all user roles

## Expected Responses

### Successful Authentication:
```json
{
  "authenticated": true,
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "name": "John Doe",
  "roles": ["ADMIN", "USER"],
  "permissions": ["user:read", "user:write", "product:read", "product:write"]
}
```

### Permission Test Result:
```json
{
  "permission": "product:read",
  "hasPermission": true,
  "currentUser": "John Doe"
}
```

### Access Denied Response:
```json
{
  "timestamp": "2024-11-03T12:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/products"
}
```

## Notes

1. **JWT Token**: All protected endpoints require a valid JWT token in the Authorization header
2. **Permission Hierarchy**: Some operations may require multiple permissions (e.g., price changes require both update and write permissions)
3. **Error Handling**: The system returns appropriate HTTP status codes (401 for unauthorized, 403 for forbidden)
4. **Session Management**: The application uses stateless JWT tokens (no server-side sessions)

This permission system provides both declarative (annotation-based) and programmatic ways to control access to resources based on the current user's role and permissions.