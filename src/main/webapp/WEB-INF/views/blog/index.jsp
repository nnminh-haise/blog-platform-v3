<%--
  Created by IntelliJ IDEA.
  User: nnminh
  Date: 06/04/2024
  Time: 12:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
  <title>Blogs</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
  <script src="${pageContext.request.contextPath}/js/blog.js"></script>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<nav class="topnav navbar navbar-expand-lg navbar-light bg-white fixed-top">
  <div class="container">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/blogs/index.htm"><strong>Mundana</strong></a>
    <button class="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarColor02" aria-controls="navbarColor02" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="navbar-collapse collapse" id="navbarColor02" style="">
      <ul class="navbar-nav mr-auto d-flex align-items-center">
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/blogs/index.htm">Intro <span class="sr-only">(current)</span></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="./article.html">Culture</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="./article.html">Tech</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="./article.html">Politics</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="./article.html">Health</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="./article.html">Collections</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="./about.html">About</a>
        </li>
      </ul>
    </div>
  </div>
</nav>
<div class="post-container">
  <c:forEach var="blog" items="${blogs}">
    <div class="post mb-3 d-flex justify-content-between rounded" style="display: none;">
      <div class="pr-3">
        <h2 class="mb-1 h4 font-weight-bold">
          <a class="text-dark" href="${pageContext.request.contextPath}/blog/${blog.slug}">${blog.title}</a>
        </h2>
        <p>${blog.description}</p>
<%--        <div class="card-text text-muted small">--%>
<%--            ${blog.author} in ${blog.category}--%>
<%--        </div>--%>
<%--        <small class="text-muted">${blog.create_at}</small>--%>
      </div>
<%--      <img height="120" src="${blog.image}">--%>
    </div>
  </c:forEach>
  <div class="d-flex w-100 justify-content-center">
    <button class="rounded my-2 btn-gray w-25" style="cursor: pointer" id="loadMore">Xem tiếp</button>
  </div>

</div>
<div class="container mt-5">
  <footer class="bg-white border-top p-3 text-muted small">
    <div class="row align-items-center justify-content-between">
      <div>
        <span class="navbar-brand mr-2"><strong>Mundana</strong></span> Copyright &copy;
        <script>document.write(new Date().getFullYear())</script>
        . All rights reserved.
      </div>
    </div>
  </footer>
</div>
</body>
</html>
