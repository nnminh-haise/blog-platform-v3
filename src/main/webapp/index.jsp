<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Blog platform</title>
</head>
<body>
  <h1>Blog platform</h1>
  <p>Welcome</p>

  <p><a href="${pageContext.request.contextPath}/blogs/index.htm">Blogs index</a></p>
  <p><a href="${pageContext.request.contextPath}/blogs/edit.htm">Blogs edit</a></p>
  <p><a href="${pageContext.request.contextPath}/blogs/editor.htm">Editor</a></p>
<%--  <p><a href="${pageContext.request.contextPath}/blogs/editor.htm">Category</a></p>--%>
</body>
</html>