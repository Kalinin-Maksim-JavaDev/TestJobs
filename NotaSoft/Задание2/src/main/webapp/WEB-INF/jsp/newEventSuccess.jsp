<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Person List</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<h1>Events added:</h1>

<br/><br/>
<div>
    <table border="1">
        <tr>
            <th>Date</th>
            <th>Name</th>
        </tr>
        <c:forEach items="${events}" var="event">
            <tr>
                <td>${event.date}</td>
                <td>${event.name}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>

</html>