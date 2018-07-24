<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Static content -->
    <link rel="stylesheet" href="/resources/css/normalize.css">
    <link rel="stylesheet" href="/resources/css/main.css">
    <script type="text/javascript" src="/resources/js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="/resources/js/super3.js"></script>
    <title></title>
</head>
<body>

<c:set var="board" value="${board}" scope="request"/>


<table class="board">
    <c:forEach var="area" items="${board.cells}">
        <c:if test="${area.number == 3||area.number == 6||area.number == 9}">
            <tr>
        </c:if>
        <td>
            <c:set var="area" value="${area}" scope="request"/>
            <%@ include file="/WEB-INF/jspf/super3/area.jspf" %>
        </td>
        <c:if test="${area.number == 5||area.number == 8||area.number == 11}">
            </tr>
        </c:if>
    </c:forEach>
</table>
<table id="dice">
    <tr><th id="activePlayer" colspan="2"></th></tr>
    <tr>
        <td>
            <div id="dice1" class="dice"></div>
        </td>
        <td>
            <div id="dice2" class="dice">Roll</div>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <div id="rollDice"></div>
        </td>
    </tr>
</table>
<script>
    doPoll();
</script>
</body>
</html>