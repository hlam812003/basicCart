<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="./styles.css" type="text/css"/>
</head>
<body>
    <h1>CD list</h1>
    <table>
        <tr>
            <th>Description</th>
            <th class="right">Price</th>
            <th>&nbsp;</th>
        </tr>
        <c:forEach var="product" items="${products}">
            <tr>
                <td><c:out value='${product.description}'/></td>
                <td class="right">${product.priceCurrencyFormat}</td>
                <td>
                    <form action="cart" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productCode" value="<c:out value='${product.code}'/>">
                        <input type="submit" value="Add To Cart">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <div class="btn__wrapper">
		<button class="btn return__btn"><a href="https://hlam812003.github.io/hwanportfolio/#portfolio">Quay về</a></button>
		<button class="btn next__btn"><a href="./404.html" id="next-btn-link">Bài tập 5</a></button>
	</div>
</body>
</html>