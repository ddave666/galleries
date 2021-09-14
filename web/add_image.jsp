<%--
    Document   : gallery_new
    Created on : 2008-12-14, 13:49:15
    Author     : Dave
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>

    <s:set name="gallery" value="gallery"></s:set>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Dodaj zdjęcie do galerii <s:property value="%{gallery.getTitle()}"/></title>
    </head>
    <body>
        <s:label label="%{error_mesgs}"></s:label>
        <s:form action="AddImage" method="post" enctype="multipart/form-data">
        <s:hidden name="id" value="%{gallery.getTitle()}"/>
            Zdjęcie(zip lub jpg): <s:file name="image"/>
    <s:submit value="Zapisz"/>
    <s:submit value="Anuluj" name="redirect-action:index"/>
</s:form>


    </body>
</html>
