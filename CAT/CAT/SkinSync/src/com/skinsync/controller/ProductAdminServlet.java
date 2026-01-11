package com.skinsync.controller;

import com.skinsync.model.Product;
import com.skinsync.util.DatabaseManager;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ProductAdminServlet")
public class ProductAdminServlet extends HttpServlet {
    
    private static final Gson gson = new Gson();

    // GET: Retrieve all products from database
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        // Get all products from database
        List<Product> products = DatabaseManager.getAllProducts();
        
        // Convert to JSON using Gson
        String json = gson.toJson(products);
        
        out.print(json);
        out.flush();
    }

    // POST: Add new product or delete existing product
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            // Delete product from database
            String idToDelete = request.getParameter("id");
            DatabaseManager.deleteProduct(idToDelete);
        
        } else {
            // Add new product to database
            String name = request.getParameter("name");
            double price = Double.parseDouble(request.getParameter("price"));
            String category = request.getParameter("category");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String imageUrl = request.getParameter("image");
            
            // Create new product
            Product newProduct = new Product(null, name, price, category, quantity, imageUrl);
            
            // Save to database (ID will be auto-generated)
            DatabaseManager.addProduct(newProduct);
        }

        // Redirect back to admin page
        response.sendRedirect("admin.html");
    }   
}
