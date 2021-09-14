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
    <s:set name="gallery" value="gallery"></s:set>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

            <title>Galeria <s:property value="%{gallery.getTitle()}"></s:property></title>
        </head>
        <body>
            <h1>Galeria <s:property value="%{gallery.getTitle()}"></s:property>:</h1>
            <br/>
            <h2><s:property value="%{gallery.getDescription()}" /></h2>
            <s:iterator value="images">
                <a href="
                <s:url action="ShowImage">
                    <s:param name="id" value="%{gallery.getTitle()}"/>
                    <s:param name="img_name" value="Filename"/>
                </s:url>"><s:property value="Filename" /></a> - <s:property value="Date"/> <a href="
                <s:url action="DelImage">
                    <s:param name="id" value="%{gallery.getTitle()}"/>
                    <s:param name="img_name" value="Filename"/>
                </s:url>" onclick="return confirm('Czy na pewno?');">(Usuń)</a><br/>
            </s:iterator>
           <s:url id="url" action="AddImage" />
           <a href="<s:property value="#url"/>">Dodaj zdjęcie do galerii</a>
           <s:url id="url2" action="EditDescription" />
           <a href="<s:property value="#url2"/>">Zmień opis galerii</a>
           <a href="<s:url action="ListGalleries"/>"> Lista galerii </a>

        </body>
        
    </html>
