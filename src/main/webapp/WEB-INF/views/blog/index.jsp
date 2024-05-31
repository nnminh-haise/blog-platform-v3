<%-- Created by IntelliJ IDEA. User: nnminh Date: 06/04/2024 Time: 12:07 To
change this template use File | Settings | File Templates. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
  <head>
    <title>Blogs</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css"
    />
  </head>
  <body>
    <!-- <img src="${pageContext.request.contextPath}/images/background.jpg" alt="Description"> -->

    <table border="1">
      <thead>
        <tr>
          <th>ID</th>
          <th>Title</th>
          <th>Description</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="blog" items="${blogs}">
          <tr>
            <td>
              <a
                href="${pageContext.request.contextPath}/blogs/edit.htm?id=${blog.id}"
                >${blog.id}</a
              >
            </td>
            <td>${blog.title}</td>
            <td>${blog.description}</td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
    <div>
      <button>
        <a href="${pageContext.request.contextPath}/blogs/edit.htm">Edit</a>
      </button>
      <button>
        <a href="${pageContext.request.contextPath}/blogs/edit.htm">New</a>
      </button>
    </div>
  </body>
</html>
