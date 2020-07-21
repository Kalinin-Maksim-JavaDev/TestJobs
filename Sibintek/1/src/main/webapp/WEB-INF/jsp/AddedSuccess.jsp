<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Operation success</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<h1>Files added:</h1>

<br/><br/>
<div>
    <table border="1">
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Autor</th>
        </tr>
        <c:forEach items="${files}" var="file">
            <tr>
                <td>${file.getDate()}</td>
                <td>${file.getDescription()}</td>
                <td>${file.getAutor()}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>

</html>