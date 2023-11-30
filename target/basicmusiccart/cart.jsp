<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="./styles.css" type="text/css"/>
    <title>Cart Page</title>
</head>
<body>
    <h1>Your cart</h1>
    <c:choose>
      <c:when test="${cart != null and not empty cart.items}">
          <table>
            <tr>
              <th>Quantity</th>
              <th>Description</th>
              <th>Price</th>
              <th>Amount</th>
              <th></th>
            </tr>
            <c:forEach var="item" items="${cart.items}">
              <tr>
                <td>
                  <form action="" method="post">
                    <input type="hidden" name="action" value="updateQuantity">
                    <input type="hidden" name="productCode" value="<c:out value='${item.product.code}'/>">
                    <input type="text" name="quantity" value="<c:out value='${item.quantity}'/>" id="quantity">
                    <input type="submit" value="Update">
                </form>            
                </td>
                <td><c:out value='${item.product.description}'/></td>
                <td class="right">${item.product.priceCurrencyFormat}</td>
                <td class="right">${item.totalCurrencyFormat}</td>
                <td>
                  <form action="" method="post">
                    <input type="hidden" name="productCode" value="<c:out value='${item.product.code}'/>">
                    <input type="hidden" name="quantity" value="0">
                    <input type="hidden" name="action" value="remove">
                    <input type="submit" value="Remove Item">
                  </form>
                </td>
              </tr>
            </c:forEach>
        </table>

        <!-- <c:if test="${not empty checkoutError}">
          <p class="error-message" style="color: red; font-weight: bold;">${checkoutError}</p>
        </c:if> -->
    
        <p><b>Total Amount:</b> ${cart.totalCurrencyFormat}</p>
    
        <p><b>To change the quantity</b>, enter the new quantity 
          and click on the Update button.</p>
          
        <form action="cart" method="post">
            <label for="couponCode">Have a coupon?</label>
            <input type="text" id="couponCode" name="couponCode" placeholder="Enter coupon code">
            <input type="hidden" name="action" value="applyCoupon">
            <input type="submit" value="Apply">
        </form>
    
        <c:if test="${not empty sessionScope.appliedCouponCode}">
            <div class="applied-coupon" style="
            margin: 10px 0;     
            display: flex;
            align-items: center;">
                <span>Applied Coupon: ${sessionScope.appliedCouponCode}</span>
                <form action="cart" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="removeCoupon" />
                    <button type="submit" class="remove-coupon-btn" 
                    style="    
                    background: none;
                    border: none;
                    cursor: pointer;
                    color: red;
                    font-weight: bold;
                    font-size: 16px;"
                    >X</button>
                </form>
            </div>
        </c:if>
    
        <c:choose>
          <c:when test="${not empty couponMessage and fn:contains(couponMessage, 'successfully')}">
            <!-- Message for successful coupon application -->
            <p class="coupon-message-success" style="color: green; font-weight: bold;">${couponMessage}</p>
          </c:when>
          <c:when test="${not empty couponMessage and not fn:contains(couponMessage, 'successfully')}">
            <!-- Message for failed coupon application -->
            <p class="coupon-message-error" style="color: red; font-weight: bold;">${couponMessage}</p>
          </c:when>
        </c:choose>

        <div style="display: flex; align-items: center; gap: 12px;">
          <form action="" method="post">
              <input type="hidden" name="action" value="shop">
              <input type="submit" value="Continue Shopping">
          </form>
          
          <form action="" method="post">
              <input type="hidden" name="action" value="checkout">
              <input type="submit" value="Checkout">
          </form>
        </div>
      </c:when>
      <c:otherwise>
          <!-- Display the empty cart message -->
          <div class="empty-cart-message">
              <p>Your cart is empty, want to buy some?</p>
          </div>

          <form action="" method="post">
            <input type="hidden" name="action" value="shop">
            <input type="submit" value="Shop Now">
          </form>
      </c:otherwise>
    </c:choose>

</body>
</html>