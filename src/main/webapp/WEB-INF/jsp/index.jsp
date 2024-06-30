<%--
  Created by IntelliJ IDEA.
  User: lucadomeneghetti
  Date: 31/05/2024
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Welcome!</title>
    <script>
        function viewSignup() {
            window.location.href = "/signup"
        }

        function onLoadHandler() {
            document.authForm.signup.addEventListener("click", )
        }

        window.addEventListener("load", onLoadHandler);
    </script>
</head>
<body>
    <h1>Circolo Culturale</h1>
    <form name="authForm" method="post" action="login">
        <input type="text" id="cf" name="cf" maxlength="16">
        <label for="cf">Codice fiscale</label>
        <input type="password" id="password" name="password">
        <label for="password">Password</label>
        <input type="submit" name="login" value="Login">
        <input type="button" name="signup" value="Registrati" onclick="viewSignup()">
    </form>
    <table>
        <thead>
            <tr>
                <td>Nome</td>
                <td>Indirizzo</td>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${sedi}" var="sede">
                <tr>
                    <td>${sede.nome}</td>
                    <td>${sede.indirizzo}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
