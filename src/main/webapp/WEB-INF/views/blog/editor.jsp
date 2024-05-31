<%--
  Created by IntelliJ IDEA.
  User: nnminh
  Date: 20/04/2024
  Time: 10:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Extended Editor.js Tools Example</title>
  <link href="${pageContext.request.contextPath}/css/editor.css" rel="stylesheet" />

  <link
          rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
  />
  <!-- Editor.js CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/editorjs@latest"></script>

  <!-- Editor.js Header Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/header@latest"></script>

  <!-- Editor.js List Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/list@latest"></script>

  <!-- Editor.js Paragraph Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/paragraph@latest"></script>

  <!-- Editor.js Image Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/image@latest"></script>

  <!-- Editor.js Embed Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/embed@latest"></script>

  <!-- Editor.js Link Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/link@latest"></script>

  <!-- Editor.js Delimiter Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/delimiter@latest"></script>

  <!-- Editor.js Code Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/code@latest"></script>
</head>
<body>
  <form:form id="main-container" action="saver.htm" method="post" modelAttribute="createBlogDto">
    <div id="header">
      <div id="tools"></div>
      <div id="background-container">
        <img src="${pageContext.request.contextPath}/images/background.jpg" alt="background" />
      </div>
      <div id="save-icon">
        <button onclick="saveData()" id="save-blog">
          <i class="fas fa-save"></i>
        </button>
      </div>
    </div>
    <div id="editor">
      <div id="title">
        <form:input path="title" id="title" type="text" placeholder="Title"/>
        <form:input path="description" id="description" type="text" hidden="true"/>
        <form:input path="attachment" id="attachment" type="text" hidden="true"/>
      </div>
    </div>
    <div id="footer">
      <div id="error-message">
      <c:if test="${not empty errorMessage}">
        <div style="color: red;">
          <p>Status: <c:out value="${errorMessage.status}" /></p>
          <p>Error: <c:out value="${errorMessage.error}" /></p>
          <p>Message: <c:out value="${errorMessage.message}" /></p>
        </div>
      </c:if>
    </div>
    </div>
  </form:form>

  <script src="${pageContext.request.contextPath}/js/editor-settings.js"></script>
</body>
</html>
