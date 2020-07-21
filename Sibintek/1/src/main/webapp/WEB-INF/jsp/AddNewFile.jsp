<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>New files List</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<h1>New file</h1>

<br/><br/>
<div>
    <form method="POST" name="File" action="http://localhost:8080/newFile">
        Description: <input type="text" name="description"><br>
        Autor <input type="text" name="autor"><br>
        <input type="submit" value="Accept">
    </form>
</div>
</body>

</html>