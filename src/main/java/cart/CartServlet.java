package cart;

import java.io.*;
import data.*;
import business.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class CartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        ServletContext sc = getServletContext();
        
        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "cart";  // default action
        }

        // perform action and set URL to appropriate page
        String url = "/index.jsp";
        if (action.equals("shop")) {            
            url = "/index.jsp";    // the "index" page
        } else if (action.equals("cart")) {
            String productCode = request.getParameter("productCode");
            String quantityString = request.getParameter("quantity");

            HttpSession session = request.getSession();
            Cart cart;
            final Object lock = request.getSession().getId().intern();
            synchronized(lock) {
                cart = (Cart) session.getAttribute("cart");
            }
            if (cart == null) {
                cart = new Cart();
            }

            //if the user enters a negative or invalid quantity,
            //the quantity is automatically reset to 1.
            int quantity = 1;
            try {
                quantity = Integer.parseInt(quantityString);
                if (quantity < 0) {
                    quantity = 1;
                }
            } catch (NumberFormatException nfe) {
                quantity = 1;
            }

            String path = sc.getRealPath("/WEB-INF/products.txt");
            Product product = ProductIO.getProduct(productCode, path);

            if(product == null) {
                System.out.println("Product not found for code: " + productCode);
            } else {
                System.out.println("Product found: " + product.getCode());
            }
            
            System.out.println("Product code: " + productCode);

            LineItem lineItem = new LineItem();
            lineItem.setProduct(product);
            lineItem.setQuantity(quantity);
            if (quantity > 0) {
                cart.addItem(lineItem);
            } else if (quantity == 0) {
                cart.removeItem(lineItem);
            }

            synchronized(lock) {
                session.setAttribute("cart", cart);
            }

            session.setAttribute("cart", cart);
            url = "/cart.jsp";
        } else if (action.equals("updateQuantity")) {
            String productCode = request.getParameter("productCode");
            String quantityString = request.getParameter("quantity");

            HttpSession session = request.getSession();
            Cart cart;
            final Object lock = request.getSession().getId().intern();
            synchronized(lock) {
                cart = (Cart) session.getAttribute("cart");
            }
            if (cart == null) {
                cart = new Cart();
            }

            int quantity = Integer.parseInt(quantityString); // Parse user input

            String path = getServletContext().getRealPath("/WEB-INF/products.txt");

            Product product = ProductIO.getProduct(productCode, path);
            // Handle the possible null product
            if (product != null) {
                LineItem lineItem = new LineItem();
                lineItem.setProduct(product);
                lineItem.setQuantity(quantity);
                cart.updateItem(lineItem); 
            } else {
                request.setAttribute("errorMessage", "Product not found for code: " + productCode);
            }

            session.setAttribute("cart", cart);
            url = "/cart.jsp";     
        } else if (action.equals("checkout")) {
            url = "/checkout.jsp";
        } else if (action.equals("completeCheckout")) {
            String customerName = request.getParameter("customerName");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String phoneNumberString = request.getParameter("phoneNumber");
            int phoneNumber = 0;
            try {
                phoneNumber = Integer.parseInt(phoneNumberString);
            } catch (NumberFormatException e) {

                e.printStackTrace();
            }
        
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
        
            // Chuyển thông tin đơn hàng và giỏ hàng vào session để truy cập trong JSP
            session.setAttribute("customerName", customerName);
            session.setAttribute("email", email);
            session.setAttribute("phoneNumber", phoneNumber);
            session.setAttribute("address", address);
        
            // Test in thông tin đơn hàng
            System.out.println("Name: " + customerName);
            System.out.println("Email: " + email);
            System.out.println("Address: " + address);
            System.out.println("Cart: " + cart);
        
            // Xóa giỏ hàng sau khi thanh toán
            // session.removeAttribute("cart");
        
            // Chuyển hướng đến trang xác nhận
            url = "/confirmation.jsp";
        } else if (action.equals("clearCart")) {
            HttpSession session = request.getSession();
            session.removeAttribute("cart");
            url = "/index.jsp";    
        } else if (action.equals("applyCoupon")) {
            String couponCode = request.getParameter("couponCode");
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            
            if (cart != null && couponCode != null && !couponCode.trim().isEmpty()) {
                double discount = CouponIO.getCouponDiscount(couponCode, getServletContext().getRealPath("/WEB-INF/coupons.txt"));
                
                if (discount > 0) {
                    cart.applyDiscount(discount); // Áp dụng giảm giá
                    session.setAttribute("cart", cart);
                    request.setAttribute("couponMessage", "Mã giảm giá được áp dụng thành công!");
                } else {
                    request.setAttribute("couponMessage", "Mã giảm giá không hợp lệ.");
                }
            } else {
                request.setAttribute("couponMessage", "Lỗi: Không tìm thấy giỏ hàng hoặc không nhập mã giảm giá.");
            }
            
            url = "/cart.jsp";
        }

        sc.getRequestDispatcher(url)
                .forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }    
}
