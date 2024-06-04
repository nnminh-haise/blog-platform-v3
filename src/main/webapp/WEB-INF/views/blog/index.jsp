<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
  <title>Blogs</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
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
<div class="container">
  <div class="row justify-content-between">
    <div class="col-md-8">
      <h5 class="font-weight-bold spanborder"><span>All Stories</span></h5>
      <div class="post-container">
        <c:forEach var="blog" items="${blogs}">
          <div class="post mb-3 d-flex justify-content-between rounded" style="display: none !important;">
            <div class="pr-3">
              <h2 class="mb-1 h4 font-weight-bold">
                <a class="text-dark" href="${pageContext.request.contextPath}/blog/${blog.slug}">${blog.title}</a>
              </h2>
              <p>${blog.description}</p>
            </div>
            <img height="120" src="${pageContext.request.contextPath}/img/demo/blog8.jpg">
          </div>
        </c:forEach>
        <div class="d-flex w-100 justify-content-center">
          <button class="rounded my-2 btn-gray w-25" style="cursor: pointer" id="loadMore">Xem tiếp</button>
        </div>
      </div>
    </div>
      <div class="col-md-4 pl-4">
          <h5 class="font-weight-bold spanborder"><span>Popular</span></h5>
          <ol class="list-featured">
              <c:forEach var="article" items="${popularArticles}">
                  <li>
            <span>
              <h6 class="font-weight-bold">
                <a href="${article.url}" class="text-dark">${article.title}</a>
              </h6>
              <p class="text-muted">
                ${article.author} in ${article.category}
              </p>
            </span>
                  </li>
              </c:forEach>
          </ol>
      </div>
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
<script>
  $(document).ready(function(){
    // Initially show only 5 posts
    $(".post").slice(0, 5).show();

    $("#loadMore").click(function(e){
      e.preventDefault();
      // Show next 5 posts on click
      $(".post:hidden").slice(0, 5).slideDown();
      // Hide the "Show More" button if all posts are visible
      if($(".post:hidden").length === 0) {
        $("#loadMore").fadeOut('slow');
      }
    });
  });
</script>
</body>
</html>