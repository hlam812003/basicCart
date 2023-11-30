package cart;

import java.io.*;
import data.*;
import business.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class CartServlet extends HttpServlet {

    protected void add(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        // add 1 to cart

        String productCode = request.getParameter("productCode");
        int quantity = 1;

        ServletContext sc = getServletContext();

        String path = sc.getRealPath("/WEB-INF/products.txt");
        Product product = ProductIO.getProduct(productCode, path);

        LineItem lineItem = new LineItem();
        lineItem.setProduct(product);
        lineItem.setQuantity(quantity);

        HttpSession session = request.getSession();
        Cart cart;
        final Object lock = request.getSession().getId().intern();
        synchronized (lock) {
            cart = (Cart) session.getAttribute("cart");
        }

        if (cart == null) {
            cart = new Cart();
        }

        cart.addItem(lineItem);

        synchronized (lock) {
            session.setAttribute("cart", cart);
        }

        response.sendRedirect(request.getContextPath() + "/cart");
        return; // don't forget this!
    }

    protected void update(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String productCode = request.getParameter("productCode");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        // get cart in session
        HttpSession session = request.getSession();
        Cart cart;
        final Object lock = request.getSession().getId().intern();

        synchronized (lock) {
            cart = (Cart) session.getAttribute("cart");
        }

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

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void applyCoupon(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String couponCode = request.getParameter("couponCode");
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart != null && couponCode != null && !couponCode.trim().isEmpty()) {
            double discount = CouponIO.getCouponDiscount(couponCode,
                    getServletContext().getRealPath("/WEB-INF/coupons.txt"));

            if (discount > 0) {
                cart.setDiscount(discount); // Apply discount
                session.setAttribute("cart", cart);
                session.setAttribute("appliedCouponCode", couponCode); // Save the applied coupon code
                request.setAttribute("couponMessage", "Coupon code applied successfully!");
            } else {
                request.setAttribute("couponMessage", "Invalid coupon code.");
            }
        } else {
            request.setAttribute("couponMessage", "Error! Please enter a coupon code");
        }

        // response.sendRedirect(request.getContextPath() + "/cart");
        String url = "/cart.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }

    private void removeCoupon(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart != null) {
            cart.setDiscount(0); // Reset discount
            session.removeAttribute("appliedCouponCode"); // Remove the saved coupon code
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void removeItem(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String productCode = request.getParameter("productCode");
        ServletContext sc = getServletContext();

        String path = sc.getRealPath("/WEB-INF/products.txt");
        Product product = ProductIO.getProduct(productCode, path);

        LineItem lineItem = new LineItem();
        lineItem.setProduct(product);

        HttpSession session = request.getSession();
        Cart cart;
        synchronized (request.getSession().getId().intern()) {
            cart = (Cart) session.getAttribute("cart");
        }

        if (cart != null) {
            cart.removeItem(lineItem);
    
            // After removing the item, check if the cart is empty
            if (cart.getCount() == 0) {
                cart.setDiscount(0); // Reset discount
                session.removeAttribute("couponMessage"); // Remove any coupon messages
                session.removeAttribute("appliedCouponCode"); // Remove the applied coupon code
            }
    
            session.setAttribute("cart", cart);
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext sc = getServletContext();
        String action = request.getParameter("action");

        System.out.println(action);

        if (action.equals("add")) {
            this.add(request, response);
            return;
        } else if (action.equals("updateQuantity")) {
            this.update(request, response);
            return;
        } else if (action.equals("applyCoupon")) {
            this.applyCoupon(request, response);
            return;
        } else if (action.equals("removeCoupon")) {
            this.removeCoupon(request, response);
            return;
        } else if (action.equals("remove")) {
            this.removeItem(request, response);
            return;
        }

        // get current action
        if (action == null) {
            action = "cart"; // default action
        }

        // perform action and set URL to appropriate page
        String url = "/index.jsp";
        if (action.equals("shop")) {
            url = "/index.jsp"; // the "index" page
        } else if (action.equals("cart")) {
            String productCode = request.getParameter("productCode");
            String quantityString = request.getParameter("quantity");

            HttpSession session = request.getSession();
            Cart cart;
            final Object lock = request.getSession().getId().intern();
            synchronized (lock) {
                cart = (Cart) session.getAttribute("cart");
            }
            if (cart == null) {
                cart = new Cart();
            }

            // if the user enters a negative or invalid quantity,
            // the quantity is automatically reset to 1.
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

            if (product == null) {
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

            synchronized (lock) {
                session.setAttribute("cart", cart);
            }

            session.setAttribute("cart", cart);
            url = "/cart.jsp";

        } else if (action.equals("checkout")) {
            // url = "/checkout.jsp";
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
    
            // Check if cart is not null and has items
            if (cart != null && cart.getCount() > 0) {
                url = "/checkout.jsp"; // Proceed to checkout if cart has items
            } else {
                // If cart is empty, set an error message and redirect to cart page
                // request.setAttribute("checkoutError", "Your cart is empty. Please add at least 1 item before checking out.");
                url = "/cart.jsp";
            }

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
        } 

        sc.getRequestDispatcher(url)
                .forward(request, response);
    }

    protected void show(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = null;

        final Object lock = request.getSession().getId().intern();
        synchronized (lock) {
            cart = (Cart) session.getAttribute("cart");
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/cart.jsp";

        request.getRequestDispatcher(url)
                .forward(request, response);
    }
}
