package com.skinsync.controller;

import com.skinsync.model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Receive data from cart.html form
        String cartJson = request.getParameter("cartData");
        String totalPayment = request.getParameter("totalPayment");
        
        // Parse cart items from JSON
        Type listType = new TypeToken<List<CartItem>>(){}.getType();
        List<CartItem> cartItems = gson.fromJson(cartJson, listType);
        
        // Generate order ID and date
        String orderId = "ORD-" + (System.currentTimeMillis() / 1000);
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

        // 2. Prepare receipt HTML
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Order Confirmation - SkinSync</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("<style>");
        out.println(".receipt-box { background: white; max-width: 600px; margin: 50px auto; padding: 40px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); }");
        out.println(".success-icon { font-size: 50px; color: #27ae60; margin-bottom: 20px; text-align: center; }");
        out.println(".order-header { text-align: center; margin-bottom: 30px; }");
        out.println(".order-details { text-align: left; margin: 30px 0; border-top: 2px dashed #eee; padding-top: 20px; }");
        out.println(".row { display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 14px; }");
        out.println(".product-list { margin: 20px 0; }");
        out.println(".product-item { display: flex; justify-content: space-between; padding: 15px; background: #f9f9f9; border-radius: 8px; margin-bottom: 10px; }");
        out.println(".product-name { font-weight: 600; color: #2f3e3b; }");
        out.println(".product-qty { color: #666; font-size: 13px; }");
        out.println(".product-price { font-weight: 600; color: #27ae60; }");
        out.println(".shipping-status { background: #e8f3f1; color: #27ae60; padding: 15px; border-radius: 8px; text-align: center; margin: 20px 0; font-weight: 600; }");
        out.println(".total-section { border-top: 2px solid #eee; padding-top: 15px; margin-top: 15px; }");
        out.println(".total-row { display: flex; justify-content: space-between; font-size: 1.3rem; font-weight: 700; color: #2f3e3b; margin-top: 10px; }");
        out.println("</style>");
        out.println("</head><body>");

        // Receipt content
        out.println("<div class='receipt-box'>");
        out.println("<div class='success-icon'>✓</div>");
        out.println("<div class='order-header'>");
        out.println("<h2 style='color:#2f3e3b; margin-bottom: 5px;'>Payment Successful!</h2>");
        out.println("<p style='color:#888'>Thank you for shopping with SkinSync.</p>");
        out.println("</div>");

        // Order information
        out.println("<div class='order-details'>");
        out.println("<div class='row'><span>Order ID:</span> <strong>" + orderId + "</strong></div>");
        out.println("<div class='row'><span>Date:</span> <span>" + date + "</span></div>");
        out.println("<div class='row'><span>Payment Method:</span> <span>Online Banking</span></div>");
        out.println("</div>");

        // Shipping status
        out.println("<div class='shipping-status'>");
        out.println("📦 Status: In Shipping");
        out.println("</div>");

        // Product list
        out.println("<h3 style='margin-top: 20px; color: #2f3e3b;'>Order Items</h3>");
        out.println("<div class='product-list'>");
        
        if (cartItems != null && !cartItems.isEmpty()) {
            for (CartItem item : cartItems) {
                out.println("<div class='product-item'>");
                out.println("<div>");
                out.println("<div class='product-name'>" + item.getName() + "</div>");
                out.println("<div class='product-qty'>Quantity: " + item.getQty() + "</div>");
                out.println("</div>");
                out.println("<div class='product-price'>RM " + String.format("%.2f", item.getPrice() * item.getQty()) + "</div>");
                out.println("</div>");
            }
        }
        
        out.println("</div>");

        // Total section
        out.println("<div class='total-section'>");
        out.println("<div class='total-row'><span>Total Paid:</span> <strong>RM " + totalPayment + "</strong></div>");
        out.println("</div>");

        // Action buttons
        out.println("<div style='text-align: center; margin-top: 30px;'>");
        out.println("<a href='index.html' class='btn btn-primary' onclick='localStorage.removeItem(\"skinsync_cart\")' style='text-decoration: none; display: inline-block; padding: 12px 30px;'>Back to Home</a>");
        out.println("</div>");
        
        out.println("</div>");

        out.println("</body></html>");
    }
}
