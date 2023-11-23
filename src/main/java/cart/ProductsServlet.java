package cart;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.ArrayList;

import data.ProductIO;
import business.Product;

public class ProductsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        String path = getServletContext().getRealPath("/WEB-INF/products.txt");
        ArrayList<Product> products = ProductIO.getProducts(path);
        System.out.println(products.get(1).getCode());
        session.setAttribute("products", products);

        String url = "/index.jsp";
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}
