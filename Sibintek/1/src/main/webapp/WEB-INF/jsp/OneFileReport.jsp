<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Report</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<h1>File:</h1>

<br/><br/>
<div>
    <table border="1">
        <tr>
            <th>Date</th>
            <th>Autor</th>
            <th>Description</th>
        </tr>
        <c:forEach items="${files}" var="file">
            <tr>
                <td>${file.time()}</td>
                <td>${file.getAutor()}
                <td>${file.getDescription()}</td>
            </tr>
        </c:forEach>
        
    </table>
</div>
</body>

</html>