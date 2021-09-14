<%-- 
    Document   : login_form
    Created on : 2008-12-11, 21:36:52
    Author     : Dave
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Menedzer foto - bibliotek - zaloguj się</title>
    </head>

    <body>
        <p>Podaj dane potrzebne do zalogowania się na konto pocztowe, gdzie będą
        przechowywane biblioteki zdjęć</p>
        <form name="login_form" action="Login" method="POST">
            <% String errors_m = (String) request.getAttribute("error_message");%>
            <% if (errors_m != null) {%>
            <%= errors_m%>
            <% }%><br/>
            Adres serwera POP3: <input type="text" name="pop_address" value="" /><br/>
            Adres serwera SMTP: <input type="text" name="smtp_address" value="" /><br/>
            Adres email: <input type="text" name="email_address" value="" /><br/>
            Login: <input type="text" name="login_name" value="" /><br/>
            Hasło: <input type="password" name="pass" value="" /><br/>
            Maxymalny rozmiar załącznika (w KB): <input type="text" name="att_size" value="" /><br/>
            <input type="submit" value="Zatwierdź" name="submit" />
        </form>
    </body>
</html>
