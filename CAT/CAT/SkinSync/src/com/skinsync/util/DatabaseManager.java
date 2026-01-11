package com.skinsync.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.skinsync.model.User;
import com.skinsync.model.Product;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * DatabaseManager - Handles JSON file-based storage for users and products
 * Uses Gson library for JSON serialization/deserialization
 * Thread-safe with ReadWriteLock for concurrent access
 */
public class DatabaseManager {
    
    private static final String DATA_DIR = System.getProperty("catalina.base") + "/data/";
    private static final String USERS_FILE = DATA_DIR + "users.json";
    private static final String PRODUCTS_FILE = DATA_DIR + "products.json";
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    // Thread-safe locks for concurrent access
    private static final ReadWriteLock userLock = new ReentrantReadWriteLock();
    private static final ReadWriteLock productLock = new ReentrantReadWriteLock();
    
    // Initialize data directory
    static {
        try {
            File dataDir = new File(DATA_DIR);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            // Initialize with default data if files don't exist
            if (!new File(USERS_FILE).exists()) {
                initializeDefaultUsers();
            }
            if (!new File(PRODUCTS_FILE).exists()) {
                initializeDefaultProducts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ==================== USER OPERATIONS ====================
    
    /**
     * Get all users from database
     */
    public static List<User> getAllUsers() {
        userLock.readLock().lock();
        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            
            String json = new String(Files.readAllBytes(Paths.get(USERS_FILE)));
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> users = gson.fromJson(json, listType);
            return users != null ? users : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            userLock.readLock().unlock();
        }
    }
    
    /**
     * Save all users to database
     */
    public static boolean saveAllUsers(List<User> users) {
        userLock.writeLock().lock();
        try {
            String json = gson.toJson(users);
            Files.write(Paths.get(USERS_FILE), json.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            userLock.writeLock().unlock();
        }
    }
    
    /**
     * Add a new user to database
     */
    public static boolean addUser(User user) {
        List<User> users = getAllUsers();
        
        // Check if email already exists
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                return false; // Email already exists
            }
        }
        
        // Generate new ID
        int maxId = 0;
        for (User u : users) {
            if (u.getId() > maxId) {
                maxId = u.getId();
            }
        }
        user.setId(maxId + 1);
        
        users.add(user);
        return saveAllUsers(users);
    }
    
    /**
     * Find user by email and password
     */
    public static User findUser(String email, String password) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Find user by email only
     */
    public static User findUserByEmail(String email) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
    
    // ==================== PRODUCT OPERATIONS ====================
    
    /**
     * Get all products from database
     */
    public static List<Product> getAllProducts() {
        productLock.readLock().lock();
        try {
            File file = new File(PRODUCTS_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            
            String json = new String(Files.readAllBytes(Paths.get(PRODUCTS_FILE)));
            Type listType = new TypeToken<ArrayList<Product>>(){}.getType();
            List<Product> products = gson.fromJson(json, listType);
            return products != null ? products : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            productLock.readLock().unlock();
        }
    }
    
    /**
     * Save all products to database
     */
    public static boolean saveAllProducts(List<Product> products) {
        productLock.writeLock().lock();
        try {
            String json = gson.toJson(products);
            Files.write(Paths.get(PRODUCTS_FILE), json.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            productLock.writeLock().unlock();
        }
    }
    
    /**
     * Add a new product to database
     */
    public static boolean addProduct(Product product) {
        List<Product> products = getAllProducts();
        
        // Generate new ID if not set
        if (product.getId() == null || product.getId().isEmpty()) {
            int maxId = 0;
            for (Product p : products) {
                try {
                    int id = Integer.parseInt(p.getId().substring(1)); // Remove 'p' prefix
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (Exception e) {
                    // Skip invalid IDs
                }
            }
            product.setId("p" + (maxId + 1));
        }
        
        products.add(product);
        return saveAllProducts(products);
    }
    
    /**
     * Delete product by ID
     */
    public static boolean deleteProduct(String productId) {
        List<Product> products = getAllProducts();
        boolean removed = products.removeIf(p -> p.getId().equals(productId));
        if (removed) {
            return saveAllProducts(products);
        }
        return false;
    }
    
    /**
     * Find product by ID
     */
    public static Product findProductById(String productId) {
        List<Product> products = getAllProducts();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }
    
    /**
     * Update product quantity
     */
    public static boolean updateProductQuantity(String productId, int newQuantity) {
        List<Product> products = getAllProducts();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setQuantity(newQuantity);
                return saveAllProducts(products);
            }
        }
        return false;
    }
    
    // ==================== INITIALIZATION ====================
    
    /**
     * Initialize default users (admin and test user)
     */
    private static void initializeDefaultUsers() {
        List<User> users = new ArrayList<>();
        
        // Admin user
        User admin = new User();
        admin.setId(1);
        admin.setFullName("Admin SkinSync");
        admin.setEmail("admin@skinsync.com");
        admin.setPassword("admin123");
        admin.setRole("admin");
        users.add(admin);
        
        // Test customer user
        User customer = new User();
        customer.setId(2);
        customer.setFullName("Ali Customer");
        customer.setEmail("user@skinsync.com");
        customer.setPassword("123");
        customer.setRole("customer");
        users.add(customer);
        
        saveAllUsers(users);
        System.out.println("Initialized default users in database");
    }
    
    /**
     * Initialize default products
     */
    private static void initializeDefaultProducts() {
        List<Product> products = new ArrayList<>();
        
        products.add(new Product("p1", "Gentle Cleanser", 59.00, "Cleanser", 10, "https://via.placeholder.com/150"));
        products.add(new Product("p2", "Hydrating Serum", 89.00, "Serum", 5, "https://via.placeholder.com/150"));
        products.add(new Product("p3", "Niacinamide Serum 10%", 55.00, "Serum", 15, "https://via.placeholder.com/150"));
        products.add(new Product("p4", "Oil-Free Gel Moisturizer", 40.00, "Moisturizer", 20, "https://via.placeholder.com/150"));
        products.add(new Product("p5", "Matte Sunscreen SPF50", 60.00, "Sunscreen", 12, "https://via.placeholder.com/150"));
        
        saveAllProducts(products);
        System.out.println("Initialized default products in database");
    }
}
