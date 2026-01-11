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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Find user in database
        User user = DatabaseManager.findUser(email, password);
        
        if (user != null) {
            // Login successful - create session
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            
            // Redirect based on role
            if ("admin".equals(user.getRole())) {
                response.sendRedirect("admin.html");
            } else {
                response.sendRedirect("index.html");
            }
        } else {
            // Login failed - invalid credentials
            response.sendRedirect("login.html?status=invalid");
        }
    }
}
