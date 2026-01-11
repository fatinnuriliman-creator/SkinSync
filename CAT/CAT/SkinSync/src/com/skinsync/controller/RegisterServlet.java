package com.skinsync.controller;

import com.skinsync.model.User;
import com.skinsync.util.DatabaseManager;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Check if email already exists
        User existingUser = DatabaseManager.findUserByEmail(email);
        if (existingUser != null) {
            // Email already registered
            response.sendRedirect("register.html?status=exists");
            return;
        }
        
        // Create new user
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole("customer"); // Default role is customer
        
        // Save to database
        boolean success = DatabaseManager.addUser(newUser);
        
        if (success) {
            // Registration successful - redirect to login page
            response.sendRedirect("login.html?status=registered");
        } else {
            // Registration failed
            response.sendRedirect("register.html?status=error");
        }
    }
}
