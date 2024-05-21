<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <title>Greeter</title>
    </head>
    <body>
		<h1>Hello World!</h1>
		<h2>Hello <%= request.getAttribute("name")%></h2>
		<h2>Hello ${name}, using JSP!</h2>
    </body>
</html>
