<%-- Created by IntelliJ IDEA. User: nnminh Date: 31/5/24 Time: 12:20 To change
this template use File | Settings | File Templates. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Blog platform</title>
    <link
      rel="stylesheet"
      href="https://use.fontawesome.com/releases/v5.3.1/css/all.css"
      integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU"
      crossorigin="anonymous"
    />
    <!-- Main CSS -->
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/main.css"
    />
  </head>
  <body>
    <nav class="topnav navbar navbar-expand-lg navbar-light bg-white fixed-top">
      <div class="container">
        <a
          class="navbar-brand"
          href="${pageContext.request.contextPath}/blogs/index.htm"
          ><strong>Mundana</strong></a
        >
        <button
          class="navbar-toggler collapsed"
          type="button"
          data-toggle="collapse"
          data-target="#navbarColor02"
          aria-controls="navbarColor02"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="navbar-collapse collapse" id="navbarColor02" style="">
          <ul class="navbar-nav mr-auto d-flex align-items-center">
            <c:forEach var="category" items="${categories}">
              <li class="nav-item">
                <a
                  class="nav-link"
                  href="${pageContext.request.contextPath}/blogs/index.htm?category=${category.slug}"
                >
                  ${category.name}
                  <span class="sr-only">(current)</span>
                </a>
              </li>
            </c:forEach>
          </ul>
        </div>
      </div>
    </nav>
    <!-- End Navbar -->

    <!--------------------------------------
HEADER
--------------------------------------->
    <div class="container">
      <div
        class="jumbotron jumbotron-fluid mb-3 pt-0 pb-0 bg-lightblue position-relative"
      >
        <div class="pl-4 pr-0 h-100 tofront">
          <div class="row justify-content-between">
            <div class="col-md-6 pt-6 pb-6 align-self-center">
              <h1 class="secondfont mb-3 font-weight-bold">
                Mundana is an HTML Bootstrap Template for Professional Blogging
              </h1>
              <p class="mb-3">
                Beautifully crafted with the latest technologies, SASS &
                Bootstrap 4.1.3, Mundana is the perfect design for your
                professional blog. Homepage, post article and category layouts
                available.
              </p>
              <a href="./article.html" class="btn btn-dark">Read More</a>
            </div>
            <div
              class="col-md-6 d-none d-md-block pr-0"
              style="
                background-size: cover;
                background-image: url(img/demo/home.jpg);
              "
            ></div>
          </div>
        </div>
      </div>
    </div>
    <!-- End Header -->

    <!--------------------------------------
MAIN
--------------------------------------->

    <div class="container pt-4 pb-4">
      <div class="row">
        <div class="col-lg-6">
          <div class="card border-0 mb-4 box-shadow h-xl-300">
            <div
              style="
                background-image: url(./assets/img/demo/1.jpg);
                height: 150px;
                background-size: cover;
                background-repeat: no-repeat;
              "
            ></div>
            <div
              class="card-body px-0 pb-0 d-flex flex-column align-items-start"
            >
              <h2 class="h4 font-weight-bold">
                <a class="text-dark" href="./article.html"
                  >Brain Stimulation Relieves Depression Symptoms</a
                >
              </h2>
              <p class="card-text">
                Researchers have found an effective target in the brain for
                electrical stimulation to improve mood in people suffering from
                depression.
              </p>
              <div>
                <small class="d-block"
                  ><a class="text-muted" href="./author.html"
                    >Favid Rick</a
                  ></small
                >
                <small class="text-muted">Dec 12 &middot; 5 min read</small>
              </div>
            </div>
          </div>
        </div>
        <div class="col-lg-6">
          <div class="flex-md-row mb-4 box-shadow h-xl-300">
            <div class="mb-3 d-flex align-items-center rounded-lg">
              <img height="80" src="img/demo/blog4.jpg" />
              <div class="pl-3">
                <h2 class="mb-2 h6 font-weight-bold">
                  <a class="text-dark" href="./article.html"
                    >Nasa's IceSat space laser makes height maps of Earth</a
                  >
                </h2>
                <div class="card-text text-muted small">
                  Jake Bittle in LOVE/HATE
                </div>
                <small class="text-muted">Dec 12 &middot; 5 min read</small>
              </div>
            </div>
            <div class="mb-3 d-flex align-items-center rounded-lg">
              <img height="80" src="img/demo/blog5.jpg" />
              <div class="pl-3">
                <h2 class="mb-2 h6 font-weight-bold">
                  <a class="text-dark" href="./article.html"
                    >Underwater museum brings hope to Lake Titicaca</a
                  >
                </h2>
                <div class="card-text text-muted small">
                  Jake Bittle in LOVE/HATE
                </div>
                <small class="text-muted">Dec 12 &middot; 5 min read</small>
              </div>
            </div>
            <div class="mb-3 d-flex align-items-center rounded-lg">
              <img height="80" src="img/demo/blog6.jpg" />
              <div class="pl-3">
                <h2 class="mb-2 h6 font-weight-bold">
                  <a class="text-dark" href="./article.html"
                    >Sun-skimming probe starts calling home</a
                  >
                </h2>
                <div class="card-text text-muted small">
                  Jake Bittle in LOVE/HATE
                </div>
                <small class="text-muted">Dec 12 &middot; 5 min read</small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <%-- Main section   --%>
    <div class="container">
      <div class="row justify-content-between">
        <div class="col-md-8">
          <h5 class="font-weight-bold spanborder"><span>All Stories</span></h5>
          <c:forEach var="blog" items="${blogs}">
            <div class="mb-3 d-flex justify-content-between rounded-lg">
              <div class="pr-3">
                <h2 class="mb-1 h4 font-weight-bold">
                  <a class="text-dark" href="./article.html">${blog.title}</a>
                </h2>
                <p>${blog.subTitle}</p>
                <div class="card-text text-muted small">Author</div>
                <small class="text-muted">${blog.publishAt}</small>
              </div>
              <img height="120" src="${blog.thumbnail}" />
            </div>
          </c:forEach>
        </div>

        <%-- POPULAR BLOG SECTION --%>
        <div class="col-md-4 pl-4">
          <h5 class="font-weight-bold spanborder"><span>Popular</span></h5>
          <ol class="list-featured">
            <c:forEach var="blog" items="${popularBlogs}">
              <li>
              <span>
                <h6 class="font-weight-bold">
                  <a href="${pageContext.request.contextPath}/blogs/post.htm?slug=${blog.slug}" class="text-dark"
                  >${blog.title}</a
                  >
                </h6>
                <p class="text-muted">${blog.subTitle}</p>
              </span>
              </li>
            </c:forEach>
          </ol>
        </div>
      </div>
    </div>

    <!--------------------------------------
FOOTER
--------------------------------------->
    <div class="container mt-5">
      <footer class="bg-white border-top p-3 text-muted small">
        <div class="row align-items-center justify-content-between">
          <div>
            <span class="navbar-brand mr-2"><strong>Mundana</strong></span>
            Copyright &copy;
            <script>
              document.write(new Date().getFullYear());
            </script>
            . All rights reserved.
          </div>
        </div>
      </footer>
    </div>
  </body>
</html>
