<%--
  Created by IntelliJ IDEA.
  User: nnminh
  Date: 4/6/24
  Time: 18:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Title</title>
</head>
<body>
  <div class="form-container">
    <form method="POST" action="${pageContext.request.contextPath}/file-handler/file-saving-resolver.htm" enctype="multipart/form-data">
      <h2>File upload system</h2>
      <table>
        <thead>
        <tr>
          <td>UUID</td>
          <td>Title</td>
          <td>Subtitle</td>
          <td>Create at</td>
          <td>File directory</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="blog" items="${blogs}">
          <tr>
            <td>${blog.id}</td>
            <td>${blog.title}</td>
            <td>${blog.subTitle}</td>
            <td>${blog.createAt}</td>
            <td>
              <select id="file-directory" name="file-directory">
                <c:forEach var="directory" items="${directories}">
                  <option value="${directory.key}">${directory.value}</option>
                </c:forEach>
              </select>
            </td>
            <td>
              <input name="file" id="${blog.id}" type="file">
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <input type="submit" value="Upload"/>
    </form>
  </div>
</body>
</html>
