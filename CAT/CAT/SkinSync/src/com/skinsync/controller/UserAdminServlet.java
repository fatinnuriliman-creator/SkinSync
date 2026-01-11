package com.skinsync.controller;

import com.skinsync.model.User;
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

@WebServlet("/UserAdminServlet")
public class UserAdminServlet extends HttpServlet {
    
    private static final Gson gson = new Gson();

    // GET: Retrieve all users from database
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        // Get all users from database
        List<User> users = DatabaseManager.getAllUsers();
        
        // Convert to JSON using Gson
        String json = gson.toJson(users);
        
        out.print(json);
        out.flush();
    }
}
