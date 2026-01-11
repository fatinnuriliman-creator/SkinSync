# SkinSync Database Implementation Guide

## 📊 Database Type: JSON File Storage

Your SkinSync application now uses **JSON file-based storage** with the Gson library for data persistence.

---

## 🗂️ Database Structure

### Storage Location
All data is stored in: `apache-tomcat-9.0.95/data/`

### Database Files
1. **users.json** - Stores all registered users
2. **products.json** - Stores product inventory

---

## 👥 User Database (users.json)

### User Schema
```json
{
  "id": 1,
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "customer"
}
```

### Default Users
The system initializes with two default users:

**Admin Account:**
- Email: `admin@skinsync.com`
- Password: `admin123`
- Role: `admin`

**Test Customer:**
- Email: `user@skinsync.com`
- Password: `123`
- Role: `customer`

### User Operations

#### 1. User Registration
- **Servlet:** `RegisterServlet`
- **Endpoint:** `/RegisterServlet`
- **Method:** POST
- **Process:**
  1. Checks if email already exists
  2. Creates new user with auto-generated ID
  3. Saves to `users.json`
  4. Auto-logs in the user
  5. Redirects to home page

#### 2. User Login
- **Servlet:** `LoginServlet`
- **Endpoint:** `/LoginServlet`
- **Method:** POST
- **Process:**
  1. Validates email and password against database
  2. Creates session if credentials match
  3. Redirects based on user role (admin → admin.html, customer → index.html)

---

## 🛍️ Product Database (products.json)

### Product Schema
```json
{
  "id": "p1",
  "name": "Gentle Cleanser",
  "price": 59.00,
  "category": "Cleanser",
  "quantity": 10,
  "imageUrl": "https://via.placeholder.com/150"
}
```

### Default Products
The system initializes with 5 default products:
1. Gentle Cleanser (RM 59.00)
2. Hydrating Serum (RM 89.00)
3. Niacinamide Serum 10% (RM 55.00)
4. Oil-Free Gel Moisturizer (RM 40.00)
5. Matte Sunscreen SPF50 (RM 60.00)

### Product Operations

#### 1. View All Products
- **Servlet:** `ProductAdminServlet`
- **Endpoint:** `/ProductAdminServlet`
- **Method:** GET
- **Returns:** JSON array of all products

#### 2. Add New Product
- **Servlet:** `ProductAdminServlet`
- **Endpoint:** `/ProductAdminServlet`
- **Method:** POST
- **Parameters:**
  - name
  - price
  - category
  - quantity
  - image (URL)
- **Process:**
  1. Creates new product with auto-generated ID
  2. Saves to `products.json`
  3. Redirects to admin page

#### 3. Delete Product
- **Servlet:** `ProductAdminServlet`
- **Endpoint:** `/ProductAdminServlet?action=delete&id=p1`
- **Method:** POST
- **Process:**
  1. Removes product from database
  2. Updates `products.json`
  3. Redirects to admin page

---

## 🔧 Database Manager API

### Class: `DatabaseManager`
Location: `src/com/skinsync/util/DatabaseManager.java`

### User Methods

```java
// Get all users
List<User> users = DatabaseManager.getAllUsers();

// Add new user (returns true if successful)
boolean success = DatabaseManager.addUser(user);

// Find user by email and password
User user = DatabaseManager.findUser(email, password);

// Find user by email only
User user = DatabaseManager.findUserByEmail(email);

// Save all users
boolean success = DatabaseManager.saveAllUsers(userList);
```

### Product Methods

```java
// Get all products
List<Product> products = DatabaseManager.getAllProducts();

// Add new product
boolean success = DatabaseManager.addProduct(product);

// Delete product by ID
boolean success = DatabaseManager.deleteProduct("p1");

// Find product by ID
Product product = DatabaseManager.findProductById("p1");

// Update product quantity
boolean success = DatabaseManager.updateProductQuantity("p1", 20);

// Save all products
boolean success = DatabaseManager.saveAllProducts(productList);
```

---

## 🔒 Thread Safety

The DatabaseManager uses **ReadWriteLock** for thread-safe concurrent access:
- Multiple threads can read simultaneously
- Write operations are exclusive
- Prevents data corruption in multi-user scenarios

---

## 💾 Data Persistence

### When Data is Saved
1. **User Registration** - Immediately saved to users.json
2. **Product Addition** - Immediately saved to products.json
3. **Product Deletion** - Immediately saved to products.json
4. **Server Restart** - Data persists across restarts

### Data Location
```
apache-tomcat-9.0.95/
└── data/
    ├── users.json
    └── products.json
```

---

## 🔄 How to Update the Database

### Method 1: Through the Application
- **Users:** Register through the registration page
- **Products:** Add/delete through the admin panel

### Method 2: Manual Editing
1. Stop the server
2. Navigate to `apache-tomcat-9.0.95/data/`
3. Edit `users.json` or `products.json`
4. Ensure valid JSON format
5. Restart the server

### Method 3: Reset to Defaults
1. Stop the server
2. Delete `users.json` and/or `products.json`
3. Restart the server
4. Default data will be recreated automatically

---

## 📝 Example: Adding a User Programmatically

```java
User newUser = new User();
newUser.setFullName("Jane Smith");
newUser.setEmail("jane@example.com");
newUser.setPassword("securepass");
newUser.setRole("customer");

boolean success = DatabaseManager.addUser(newUser);
if (success) {
    System.out.println("User added successfully!");
}
```

## 📝 Example: Adding a Product Programmatically

```java
Product newProduct = new Product(
    null,  // ID will be auto-generated
    "Vitamin C Serum",
    75.00,
    "Serum",
    15,
    "https://example.com/image.jpg"
);

boolean success = DatabaseManager.addProduct(newProduct);
if (success) {
    System.out.println("Product added successfully!");
}
```

---

## ⚠️ Important Notes

1. **Backup Your Data:** Regularly backup the `data/` folder
2. **JSON Format:** Ensure JSON files are valid when editing manually
3. **Concurrent Access:** The system handles multiple users safely
4. **Password Security:** Currently passwords are stored in plain text (consider hashing in production)
5. **File Permissions:** Ensure Tomcat has read/write access to the data directory

---

## 🚀 Testing the Database

### Test User Registration
1. Go to `http://localhost:8080/SkinSync/register.html`
2. Fill in the form and submit
3. Check `data/users.json` to see the new user

### Test Product Management
1. Login as admin (`admin@skinsync.com` / `admin123`)
2. Go to admin panel
3. Add a new product
4. Check `data/products.json` to see the new product

### Test Data Persistence
1. Add a user or product
2. Stop the server (Ctrl+C)
3. Restart the server (`start-server.bat`)
4. Verify the data is still there

---

## 🔍 Troubleshooting

### Data Not Saving
- Check Tomcat has write permissions to the data directory
- Verify JSON format is valid
- Check Tomcat logs for errors

### Data Not Loading
- Ensure JSON files exist in `apache-tomcat-9.0.95/data/`
- Check JSON syntax is correct
- Restart the server

### Duplicate Users
- The system prevents duplicate emails automatically
- If duplicates exist, manually edit `users.json`

---

## 📚 Additional Resources

- **Gson Documentation:** https://github.com/google/gson
- **JSON Format:** https://www.json.org/
- **Java File I/O:** https://docs.oracle.com/javase/tutorial/essential/io/

---

**Database Implementation Complete! 🎉**

Your SkinSync application now has persistent storage for users and products using JSON files.
