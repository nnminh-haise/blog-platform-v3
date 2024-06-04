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
  <form method="POST" action="${pageContext.request.contextPath}/uploadFile.htm" enctype="multipart/form-data">
    <h2>File upload system</h2>
    <table>
      <tr>
        <td>
          <label path="base-directory">All file is stored at a base directory at: ${baseDirectory}</label>
        </td>
      </tr>
      <tr>
        <td>
          <label path="file">Select a file to upload</label>
        </td>
        <td>
          <input type="file" name="file" />
        </td>
      </tr>
      <tr>
        <td>
          <label path="path-selector">Select storing path:</label>
          <select id="path-selector" name="paths">
            <c:forEach var="path" items="${directories}">
              <option value="${path.key}">${path.value}</option>
            </c:forEach>
          </select>
        </td>
      </tr>
      <tr>
        <td>
          <input type="submit" value="Upload"/>
        </td>
      </tr>
    </table>
  </form>
</body>
</html>
