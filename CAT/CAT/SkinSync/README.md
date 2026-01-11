# SkinSync - Personalized Skincare Application

A Java Servlet-based web application for personalized skincare product recommendations and e-commerce.

## Features

- 🔐 User Authentication (Admin & Customer roles)
- 🛍️ Product Catalog with Shopping Cart
- 📊 Admin Dashboard for Product Management
- 🧪 Skin Quiz for Personalized Recommendations
- 💳 Checkout System with Shipping Calculation

## Prerequisites

- Java JDK 8 or higher (✅ Java 25 detected)
- Apache Tomcat 9.x (will be downloaded automatically)
- Windows Operating System

## Quick Start

### 1. Setup (First Time Only)

Run the setup script to install Tomcat, compile the application, and deploy it:

```bash
setup.bat
```

This will:
- Extract Apache Tomcat
- Compile Java source files
- Deploy the application to Tomcat

### 2. Start the Server

```bash
start-server.bat
```

The application will be available at: **http://localhost:8080/SkinSync/**

### 3. Login Credentials

**Admin Account:**
- Email: `admin@skinsync.com`
- Password: `admin123`
- Access: Product management, admin dashboard

**Customer Account:**
- Email: `user@skinsync.com`
- Password: `123`
- Access: Shopping, cart, checkout

## Project Structure

```
SkinSync/
├── src/
│   └── com/skinsync/
│       ├── controller/          # Servlet controllers
│       │   ├── LoginServlet.java
│       │   ├── ProductAdminServlet.java
│       │   ├── QuizServlet.java
│       │   ├── RegisterServlet.java
│       │   └── CheckoutServlet.java
│       └── model/               # Data models
│           ├── User.java
│           ├── Product.java
│           └── CartItem.java
├── web/
│   ├── index.html              # Home page
│   ├── login.html              # Login page
│   ├── register.html           # Registration page
│   ├── admin.html              # Admin dashboard
│   ├── cart.html               # Shopping cart
│   ├── quiz.html               # Skin quiz
│   ├── css/
│   │   └── style.css           # Styles
│   ├── js/
│   │   └── main.js             # Frontend logic
│   ├── images/
│   │   └── background.jpg
│   └── WEB-INF/
│       ├── web.xml             # Deployment descriptor
│       └── lib/
│           └── gson-2.10.1.jar # JSON library
├── setup.bat                   # Setup script
├── start-server.bat            # Server startup script
├── compile.bat                 # Compilation script
└── README.md                   # This file
```

## Manual Compilation (Optional)

If you need to recompile the Java files:

```bash
compile.bat
```

## Troubleshooting

### Port 8080 Already in Use

If port 8080 is already in use, you can change it by editing:
`apache-tomcat-9.0.95/conf/server.xml`

Find the line:
```xml
<Connector port="8080" protocol="HTTP/1.1"
```

Change `8080` to another port (e.g., `8081`).

### Compilation Errors

Make sure:
1. Java JDK is properly installed
2. JAVA_HOME environment variable is set
3. All source files are present in the `src/` directory

### Application Not Loading

1. Check if Tomcat started successfully
2. Look for errors in `apache-tomcat-9.0.95/logs/catalina.out`
3. Verify the application is deployed in `apache-tomcat-9.0.95/webapps/SkinSync/`

## Technology Stack

- **Backend:** Java Servlets 3.1
- **Frontend:** HTML5, CSS3, JavaScript (ES6)
- **Server:** Apache Tomcat 9.x
- **Libraries:** Gson 2.10.1 (JSON processing)
- **Database:** JSON File Storage (users.json, products.json)
- **Storage Location:** `apache-tomcat-9.0.95/data/`

## Database System

### 📊 Persistent Storage
The application now uses **JSON file-based database** for persistent data storage:

- **users.json** - Stores all registered users
- **products.json** - Stores product inventory

### 🔄 Data Persistence
- All user registrations are saved permanently
- Product additions/deletions persist across server restarts
- Data survives application redeployment

### 📚 Database Documentation
For detailed database information, see **[DATABASE_GUIDE.md](DATABASE_GUIDE.md)**

### 🔧 Database Features
- Thread-safe concurrent access
- Automatic ID generation
- Email uniqueness validation
- Default data initialization
- Easy backup and restore

### 💾 Database Location
```
apache-tomcat-9.0.95/
└── data/
    ├── users.json      # User accounts
    └── products.json   # Product inventory
```

## Features in Detail

### Shopping Cart
- Add/remove products
- Quantity adjustment
- Automatic total calculation
- Free shipping on orders over RM 100

### Admin Dashboard
- Add new products
- Delete existing products
- View product inventory
- Manage product categories

### Skin Quiz
- Personalized product recommendations
- Based on skin type analysis
- Interactive questionnaire

## Development

### Adding New Servlets

1. Create a new Java file in `src/com/skinsync/controller/`
2. Extend `HttpServlet`
3. Add `@WebServlet` annotation
4. Implement `doGet()` or `doPost()` methods
5. Recompile using `compile.bat`
6. Restart the server

### Modifying Frontend

1. Edit HTML files in `web/`
2. Update CSS in `web/css/style.css`
3. Modify JavaScript in `web/js/main.js`
4. Refresh browser (no restart needed)

## License

This project is for educational purposes.

## Support

For issues or questions, please check the troubleshooting section above.

---

**Enjoy using SkinSync! 🌟**
