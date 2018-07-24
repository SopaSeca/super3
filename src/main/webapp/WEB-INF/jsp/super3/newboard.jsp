<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Static content -->
    <link rel="stylesheet" href="/resources/css/normalize.css">
    <link rel="stylesheet" href="/resources/css/main.css">
    <script type="text/javascript" src="/resources/js/app.js"></script>
    <title>New game</title>
</head>
<body>

<form action="/super3/new" method="POST">
    <label for="new-color">Kies een kleur</label>
    <input type="color" name="color" id="new-color">
    <input type="submit" value="Nieuw" class="button">
</form>

<c:forEach var="availableBoard" items="${availableBoards}">
    <hr/>
    <div class="availableboard">
        <form action="/super3/join" method="POST">
            <input type="hidden" name="boardId" value="${availableBoard.id}">
            <label for="join-color">Kies een kleur</label>
            <input type="color" name="color" id="join-color">
            <input type="submit" value=" ${availableBoard.player1.name}" class="button">
        </form>
    </div>
</c:forEach>
</body>
</html>