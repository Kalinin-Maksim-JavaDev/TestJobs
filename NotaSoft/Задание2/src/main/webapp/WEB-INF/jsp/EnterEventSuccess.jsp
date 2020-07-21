<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Person List</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<h1>Event registration</h1>

<br/><br/>
<div>
    <form method="POST" name="authorization" action="http://localhost:8080/eventsList/newSuccess">
        Description: <input type="text" name="name"><br>
        <input type="submit" value="Accept">
    </form>
</div>
</body>

</html>