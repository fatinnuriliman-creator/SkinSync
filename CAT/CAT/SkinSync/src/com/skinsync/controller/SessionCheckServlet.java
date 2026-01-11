package com.skinsync.controller;

import com.skinsync.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SessionCheckServlet")
public class SessionCheckServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        // Get current session (don't create new one)
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute("currentUser") != null) {
            // User is logged in
            User user = (User) session.getAttribute("currentUser");
            
            String json = String.format(
                "{\"loggedIn\": true, \"fullName\": \"%s\", \"email\": \"%s\", \"role\": \"%s\"}",
                user.getFullName(),
                user.getEmail(),
                user.getRole()
            );
            out.print(json);
        } else {
            // User is not logged in
            out.print("{\"loggedIn\": false}");
        }
        
        out.flush();
    }
}
