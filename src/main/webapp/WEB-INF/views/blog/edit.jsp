<%--
  Created by IntelliJ IDEA.
  User: nnminh
  Date: 18/04/2024
  Time: 13:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
  <title>Edit | Blog</title>
</head>
<body>
<div id="error-message">
  <c:if test="${not empty errorMessage}">
    <div style="color: red;">
      <p>Status: <c:out value="${errorMessage.status}" /></p>
      <p>Error: <c:out value="${errorMessage.error}" /></p>
      <p>Message: <c:out value="${errorMessage.message}" /></p>
    </div>
  </c:if>
</div>

<%--@elvariable id="createBlogDto" type="com.proj.projblogplatform.dto.createBlogDto"--%>
<form:form action="save.htm" method="post" modelAttribute="createBlogDto">
  <label for="blog-title">Title</label>
  <form:input id="blog-title" path="title" type="text" value="${createBlogDto.title}" />

  <label for="blog-description">Description</label>
  <form:input id="blog-description" path="description" type="text" value="${createBlogDto.description}" />

  <button type="submit">Submit</button>
</form:form>
</body>
</html>