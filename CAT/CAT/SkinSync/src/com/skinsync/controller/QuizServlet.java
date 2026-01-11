package com.skinsync.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/QuizServlet")
public class QuizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("skinType");     
        String concern = request.getParameter("concern");   
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String jsonResult = "[]"; 

        // SENARIO A: Kulit Berminyak (Oily) & Jerawat (Acne) -> 8 Produk
        if ("oily".equals(type) && "acne".equals(concern)) {
            jsonResult = "["
                + "{\"name\": \"Gentle Cleanser\", \"price\": 45.00, \"image\": \"images/gentle cleanser.png\", \"reason\": \"Deep cleans pores.\"},"
                + "{\"name\": \"Niacinamide 10%\", \"price\": 55.00, \"image\": \"images/niacinamide-10 .png\", \"reason\": \"Controls oil production.\"},"
                + "{\"name\": \"Hyaluronic Acid\", \"price\": 40.00, \"image\": \"images/hyalunoric-acid.png\", \"reason\": \"Hydrates without grease.\"},"
                + "{\"name\": \"Matte Sunscreen SPF50\", \"price\": 60.00, \"image\": \"images/matte-sunscreen.png\", \"reason\": \"Non-shiny protection.\"},"
                + "{\"name\": \"Clay Detox Mask\", \"price\": 50.00, \"image\": \"images/claydetox.png\", \"reason\": \"Absorbs excess sebum.\"},"
                + "{\"name\": \"Night Cream\", \"price\": 25.00, \"image\": \"images/night-cream.png\", \"reason\": \"Targets active pimples.\"},"
                + "{\"name\": \"Facial Oil\", \"price\": 15.00, \"image\": \"images/facial oil.png\", \"reason\": \"Instant shine control.\"},"
                + "{\"name\": \"Brightening Eye Cream\", \"price\": 45.00, \"image\": \"images/brightening-eye-cream.png\", \"reason\": \"Reduces dark circles.\"}"
                + "]";
        
        // SENARIO B: Kulit Kering (Dry) & Kusam (Dullness) -> 8 Produk
        } else if ("dry".equals(type) && "dullness".equals(concern)) {
            jsonResult = "["
                + "{\"name\": \"Gentle Cleanser\", \"price\": 49.00, \"image\": \"images/gentle cleanser.png\", \"reason\": \"Gentle non-stripping wash.\"},"
                + "{\"name\": \"Hyaluronic Acid Booster\", \"price\": 75.00, \"image\": \"images/hyalunoric-acid.png\", \"reason\": \"Plumps skin with water.\"},"
                + "{\"name\": \"Rich Night Repair Cream\", \"price\": 120.00, \"image\": \"images/night-cream.png\", \"reason\": \"Deep overnight repair.\"},"
                + "{\"name\": \"Rose Hip Facial Oil\", \"price\": 65.00, \"image\": \"images/facial oil.png\", \"reason\": \"Locks in moisture.\"},"
                + "{\"name\": \"Matte Sunscreen SPF50\", \"price\": 55.00, \"image\": \"images/matte-sunscreen.png\", \"reason\": \"Glowy finish protection.\"},"
                + "{\"name\": \"Niacinamide 10%\", \"price\": 89.00, \"image\": \"images/niacinamide-10 .png\", \"reason\": \"Brightens complexion.\"},"
                + "{\"name\": \"All-In-One Serum\", \"price\": 70.00, \"image\": \"images/all-in-one-serum.png\", \"reason\": \"Multi-purpose care.\"},"
                + "{\"name\": \"Brightening Eye Cream\", \"price\": 80.00, \"image\": \"images/brightening-eye-cream.png\", \"reason\": \"Intense hydration boost.\"}"
                + "]";
        
        // SENARIO C: Default (Lain-lain) -> 8 Produk
        } else {
            jsonResult = "["
                + "{\"name\": \"Gentle Daily Cleanser\", \"price\": 39.00, \"image\": \"images/gentle cleanser.png\", \"reason\": \"Safe for daily use.\"},"
                + "{\"name\": \"All-In-One Serum\", \"price\": 70.00, \"image\": \"images/all-in-one-serum.png\", \"reason\": \"Multi-purpose care.\"},"
                + "{\"name\": \"Hyaluronic Acid\", \"price\": 50.00, \"image\": \"images/hyalunoric-acid.png\", \"reason\": \"Lightweight hydration.\"},"
                + "{\"name\": \"Matte Sunscreen SPF50\", \"price\": 60.00, \"image\": \"images/matte-sunscreen.png\", \"reason\": \"No white cast.\"},"
                + "{\"name\": \"Brightening Eye Cream\", \"price\": 45.00, \"image\": \"images/brightening-eye-cream.png\", \"reason\": \"Reduces dark circles.\"},"
                + "{\"name\": \"Niacinamide 10%\", \"price\": 55.00, \"image\": \"images/niacinamide-10 .png\", \"reason\": \"Controls oil production.\"},"
                + "{\"name\": \"Night Cream\", \"price\": 35.00, \"image\": \"images/night-cream.png\", \"reason\": \"Weekly exfoliation.\"},"
                + "{\"name\": \"Facial Oil\", \"price\": 20.00, \"image\": \"images/facial oil.png\", \"reason\": \"Softens lips.\"}"
                + "]";
        }

        out.print(jsonResult);
        out.flush();
    }
}