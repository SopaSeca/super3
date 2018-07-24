<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="/resources/css/normalize.css">
    <link rel="stylesheet" href="/resources/css/main.css">
    <script type="text/javascript" src="/resources/js/app.js"></script>
    <title>Spring Boot</title>
</head>
<body>

<c:forEach var="favorite" items="${favorites}">
    <c:set var="favorite" value="${favorite}" scope="request"/>
    <%@ include file="/WEB-INF/jspf/favorites/favorite.jspf" %>
</c:forEach>

<%@ include file="/WEB-INF/jspf/favorites/addfavorite.jspf" %>
</body>
</html>