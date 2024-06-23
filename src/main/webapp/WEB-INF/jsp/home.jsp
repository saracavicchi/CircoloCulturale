<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
    <h1>Welcome</h1>
    <h2><%=request.getAttribute("socio-id")%></h2>
    <h2>${socio}</h2>
</body>
</html>
