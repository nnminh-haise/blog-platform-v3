<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://www.springframework.org/tags/form" prefix="form" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>
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
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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
                ${bannerBlog.title}
              </h1>
              <p class="mb-3">${bannerBlog.subTitle}</p>
              <a
                href="${pageContext.request.contextPath}/blogs/${bannerBlog.slug}.htm"
                class="btn btn-dark"
                >Read More</a
              >
            </div>
            <div
              class="col-md-6 d-none d-md-block pr-0"
              style="
                background-size: cover;
                background-image: url('${pageContext.servletContext.contextPath}/blogs/${bannerBlog.slug}/${bannerBlog.thumbnail}');
                /* background-image: url('${bannerBlog.thumbnail}'); */
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
                /* background-image: url('${favouriteBlogs.get(0).thumbnail}'); */
                background-image: url('${pageContext.servletContext.contextPath}/blogs/${favouriteBlogs.get(0).slug}/${favouriteBlogs.get(0).thumbnail}');
                height: 150px;
                background-size: cover;
                background-repeat: no-repeat;
              "
            ></div>
            <div
              class="card-body px-0 pb-0 d-flex flex-column align-items-start"
            >
              <h2 class="h4 font-weight-bold">
                <a
                  class="text-dark"
                  href="${pageContext.request.contextPath}/blogs/${favouriteBlogs[0].slug}.htm"
                  >${favouriteBlogs[0].title}</a
                >
              </h2>
              <p class="card-text">${favouriteBlogs[0].subTitle}</p>
              <div
                style="
                  display: flex;
                  gap: 4px;
                  align-items: center;
                  padding: 4px 8px;
                  margin-bottom: 8px;
                "
              >
                <c:forEach
                  var="category"
                  items="${favouriteBlogs[0].categories}"
                >
                  <div
                    style="
                      padding: 4px 8px;
                      background-color: #e8f3ec;
                      border-radius: 8px;
                      font-size: 14px;
                      color: #393e46;
                      font-weight: 400;
                    "
                  >
                    ${category.name}
                  </div>
                </c:forEach>
              </div>
              <small class="text-muted">
                <fmt:formatDate
                  value="${favouriteBlogs[0].createAtAsDate()}"
                  pattern="MMMM d, yyyy HH:mm:ss"
                  var="formattedCreateDate"
                />
                <c:out value="${formattedCreateDate}" />
              </small>
            </div>
          </div>
        </div>
        <div class="col-lg-6">
          <div class="flex-md-row mb-4 box-shadow h-xl-300">
            <c:forEach var="blog" items="${favouriteBlogs}" varStatus="status">
              <c:if test="${status.index > 0}">
                <div class="mb-3 d-flex align-items-center rounded-lg">
                  <!-- <img height="80" src="${blog.thumbnail}" /> -->
                  <img
                    height="80"
                    src="${pageContext.servletContext.contextPath}/blogs/${blog.slug}/${blog.thumbnail}"
                  />
                  <div class="pl-3">
                    <h2 class="mb-2 h6 font-weight-bold">
                      <a
                        class="text-dark"
                        href="${pageContext.request.contextPath}/blogs/${blog.slug}.htm"
                      >
                        ${blog.title}
                      </a>
                    </h2>
                    <div
                      style="
                        display: flex;
                        gap: 4px;
                        align-items: center;
                        padding: 4px 8px;
                        margin-bottom: 8px;
                      "
                    >
                      <c:forEach var="category" items="${blog.categories}">
                        <div
                          style="
                            padding: 2px 6px;
                            background-color: #e8f3ec;
                            border-radius: 8px;
                            font-size: 14px;
                            color: #393e46;
                            font-weight: 400;
                          "
                        >
                          ${category.name}
                        </div>
                      </c:forEach>
                    </div>
                    <small class="text-muted">
                      <fmt:formatDate
                        value="${blog.createAtAsDate()}"
                        pattern="MMMM d, yyyy HH:mm:ss"
                        var="formattedCreateDate"
                      />
                      <c:out value="${formattedCreateDate}" />
                    </small>
                  </div>
                </div>
              </c:if>
            </c:forEach>
          </div>
        </div>
      </div>
    </div>

    <%-- Main section --%>
    <div class="container">
      <div class="row justify-content-between">
        <div class="col-md-8">
          <h5 class="font-weight-bold spanborder"><span>All Stories</span></h5>
          <c:forEach var="blog" items="${featuredBlogs}">
            <div
              class="mb-3 d-flex justify-content-between rounded-lg blog"
              style="display: none !important"
            >
              <div class="pr-3">
                <h2 class="mb-1 h4 font-weight-bold">
                  <a
                    class="text-dark"
                    href="${pageContext.request.contextPath}/blogs/${blog.slug}.htm"
                    >${blog.title}</a
                  >
                </h2>
                <p>${blog.subTitle}</p>
                <div
                  style="
                    display: flex;
                    gap: 4px;
                    align-items: center;
                    padding: 4px 8px;
                    margin-bottom: 8px;
                  "
                >
                  <c:forEach var="category" items="${blog.categories}">
                    <div
                      style="
                        padding: 2px 6px;
                        background-color: #e8f3ec;
                        border-radius: 8px;
                        font-size: 14px;
                        color: #393e46;
                        font-weight: 400;
                      "
                    >
                      ${category.name}
                    </div>
                  </c:forEach>
                </div>
                <small class="text-muted">
                  <fmt:formatDate
                    value="${blog.createAtAsDate()}"
                    pattern="MMMM d, yyyy HH:mm:ss"
                    var="formattedCreateDate"
                  />
                  <c:out value="${formattedCreateDate}" />
                </small>
              </div>
              <!-- <img height="120" src="${blog.thumbnail}" /> -->
              <img
                height="120"
                src="${pageContext.servletContext.contextPath}/blogs/${blog.slug}/${blog.thumbnail}"
              />
            </div>
          </c:forEach>
          <div class="d-flex w-100 justify-content-center">
            <button
              class="rounded my-2 btn-gray w-25"
              style="
                background-color: #e8f3ec;
                border: none;
                border-radius: 8px;
              "
              id="loadMore"
            >
              Xem tiếp
            </button>
          </div>
        </div>

        <%-- POPULAR BLOG SECTION --%>
        <div class="col-md-4 pl-4">
          <h5 class="font-weight-bold spanborder"><span>Popular</span></h5>
          <ol class="list-featured">
            <c:forEach var="blog" items="${popularBlogs}">
              <li>
                <span>
                  <h6 class="font-weight-bold">
                    <a
                      href="${pageContext.request.contextPath}/blogs/${blog.slug}.htm"
                      class="text-dark"
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

    <%-- Consider removing this section --%>
    <div class="container mt-5">
      <footer class="bg-white border-top p-3 text-muted small"></footer>
    </div>
  </body>
  <script>
    $(document).ready(function () {
      // Initially show only 5 posts
      console.log("blog", $(".blog"));
      $(".blog").slice(0, 5).show();

      $("#loadMore").click(function (e) {
        e.preventDefault();
        // Show next 5 posts on click
        $(".blog:hidden").slice(0, 5).slideDown();
        // Hide the "Show More" button if all posts are visible
        if ($(".blog:hidden").length === 0) {
          $("#loadMore").fadeOut("slow");
        }
      });
    });
  </script>
</html>
