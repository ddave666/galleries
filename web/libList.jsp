<%-- 
    Document   : LibList
    Created on : 2008-12-14, 01:01:05
    Author     : Dave
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista galerii</title>
    </head>
    <body>
        <h1>Lista galerii na koncie:</h1>
        <s:iterator value="galleries">
            <s:url id="gal_url" action="ShowGallery" >
                <s:param name="id" value="%{Title}"/>
            </s:url>
            <a href="<s:property value="#gal_url"/>"><s:property value="Title" /></a><br/>
           </s:iterator>
           <s:url id="url" action="NewGallery" />
           <a href="<s:property value="#url"/>">Dodaj nową galerię</a>


    </body>
</html>
