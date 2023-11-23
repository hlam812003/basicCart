<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="./styles.css" type="text/css"/>
    <title>Confirmation Page</title>
</head>
<body>
    <h1>Thank you for your order!</h1>
    <p>Your order has been received and is being processed.</p>
    <p>Customer information:</p>
    <ul>
        <li>Name: ${customerName}</li>
        <li>Email: ${email}</li>
        <li>Phone Number: ${phoneNumber}</li>
        <li>Address: ${address}</li>
    </ul>

    <p>Your order:</p>
    <table>
        <tr>
            <th>Product Description</th>
            <th>Amount</th>
            <th>Price</th>
        </tr>
        <c:forEach var="item" items="${cart.items}">
            <tr>
                <td>${item.product.description}</td>
                <td>${item.quantity}</td>
                <td>${item.product.priceCurrencyFormat}</td>
            </tr>
        </c:forEach>
    </table>
    <p>Total: ${cart.totalCurrencyFormat}</p>

    <form action="" method="post">
        <input type="hidden" name="action" value="clearCart">
        <input type="submit" value="Return Back To Home Page">
    </form>    
</body>
</html>