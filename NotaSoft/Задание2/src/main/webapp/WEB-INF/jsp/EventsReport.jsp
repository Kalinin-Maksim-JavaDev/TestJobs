<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Person List</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<h1>Events:</h1>

<br/><br/>
<div>
    <table border="1">
        <tr>
            <th>Date</th>
            <th>Event</th>
        </tr>
        <c:forEach items="${eventsByDate.entrySet()}" var="dateList">
            <tr>
                <td>${dateList.getKey()}</td>
                <td></td>
            </tr>
            <c:forEach items="${dateList.getValue()}" var="event">
                <tr>
                    <td>${event.time()}</td>
                    <td>${event.getName()}</td>
                </tr>
            </c:forEach>
        </c:forEach>
    </table>
</div>
</body>

</html>