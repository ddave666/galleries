<%--
    Document   : gallery_new
    Created on : 2008-12-14, 13:49:15
    Author     : Dave
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Gallery</title>
    </head>
    <body>
        <s:label label="%{error_mesgs}"></s:label>
        <s:form action="EditDescription" method="post">
            <s:hidden name="gallery.title" value="%{gallery.title}" />
            <s:textfield name="gallery.description" value="%{gallery.description}" label="Opis" size="40"/>
            <s:submit value="Zapisz"/>
            <s:submit value="Anuluj" name="redirect-action:index"/>
        </s:form>


    </body>
</html>
