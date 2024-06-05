<%--
  Created by IntelliJ IDEA.
  User: nnminh
  Date: 4/6/24
  Time: 18:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Title</title>
</head>
<body>
  <h2>Submitted File</h2>
  <div class="result-containter">
    <table>
      <thead>
      <tr>
        <td>UUID</td>
        <td>Title</td>
        <td>Subtitle</td>
        <td>Create at</td>
        <td>Saved at</td>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="blog" items="${blogs}" varStatus="status">
        <tr>
          <td>${blog.id}</td>
          <td>${blog.title}</td>
          <td>${blog.subTitle}</td>
          <td>${blog.createAt}</td>
          <td>
            <c:if test="${fileCreatingResults.get(status.index).length() > 0}">
              <p>${fileCreatingResults.get(status.index)}</p>
            </c:if>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</body>
</html>
