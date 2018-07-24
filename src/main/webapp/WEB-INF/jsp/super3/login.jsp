<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Static content -->
    <link rel="stylesheet" href="/resources/css/normalize.css">
    <link rel="stylesheet" href="/resources/css/main.css">
    <script type="text/javascript" src="/resources/js/app.js"></script>
    <title>Spring Boot</title>
</head>
<body>

<form action="/super3/login" method="post">
    <table>
        <tr>
            <td><label for="login.name">Name</label></td>
            <td><input id="login.name" name="name"></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="Submit" class="button"></td>
        </tr>
    </table>
</form>
</body>
</html>