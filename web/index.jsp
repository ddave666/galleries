<%-- 
    Document   : index
    Created on : 2008-12-14, 12:28:48
    Author     : Dave
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <s:form action="ShowName">
            <s:textfield name="name"/>
            <s:submit />
        </s:form>
    </body>
</html>
