<%--
  Created by IntelliJ IDEA.
  User: nnminh
  Date: 4/6/24
  Time: 18:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Title</title>
</head>
<body>
<h2>Submitted File</h2>
<table>
  <tr>
    <td>OriginalFileName:</td>
    <td>${file.originalFilename}</td>
  </tr>
  <tr>
    <td>Type:</td>
    <td>${file.contentType}</td>
  </tr>
  <tr>
    <td>Saved at:</td>
    <td>${fileDirectory}</td>
  </tr>
</table>
</body>
</html>
