<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="./styles.css" type="text/css"/>
    <title>Checkout Page</title>
</head>
<body>
    <h1>Input your information</h1>
    <p>To complete your purchase, please enter your name, email, and address below. 
        Then, click on the Finish button.</p>
    <form action="cart" method="post">
        <input type="hidden" name="action" value="completeCheckout">
        <label class="pad_top">Name:</label>
        <input type="text" name="customerName" required>
        <br>
        <label class="pad_top">Email:</label>
        <input type="email" name="email" required>
        <br>
        <label class="pad_top">Phone Number:</label>
        <input style="
            width: 15em;
            margin-left: 0.5em;    
            margin-bottom: 0.5em;" 
            type="number" 
            name="phoneNumber" required>
        <br>
        <label class="pad_top">Address:</label>
        <input type="text" name="address" required>
        <br>
        <label>&nbsp;</label>
        <input type="submit" value="Finish" style="margin-left: 0.5em">
    </form>
    <br>
    <form action="" method="post">
        <input type="hidden" name="action" value="shop">
        <input type="submit" value="Want to buy more? Continue Shopping" style="margin-left: 0.5em">
    </form>
</body>
</html>