<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
uri="http://www.springframework.org/tags/form" prefix="form" %> <%@ page
contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
  <head>
    <title>${blog.title}</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/main.css"
    />
  </head>
  <body>
    <!--------------------------------------
NAVBAR
--------------------------------------->
    <nav class="topnav navbar navbar-expand-lg navbar-light bg-white fixed-top">
      <div class="container">
        <a
          class="navbar-brand"
          href="${pageContext.request.contextPath}/index.htm"
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
                  ${category.name}<span class="sr-only">(current)</span>
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
        class="jumbotron jumbotron-fluid mb-3 pl-0 pt-0 pb-0 bg-white position-relative"
      >
        <div class="h-100 tofront">
          <div class="row justify-content-between">
            <div class="col-md-6 pt-6 pb-6 pr-6 align-self-center">
              <c:forEach var="category" items="${blogCategoryList}">
                <p class="text-uppercase font-weight-bold">
                  <a class="text-danger" href="${pageContext.request.contextPath}/blogs/index.htm?category=${category.slug}">
                    ${category.name}
                  </a>
                </p>
              </c:forEach>
              <h1 class="display-4 secondfont mb-3 font-weight-bold">${blog.title}</h1>
              <p class="mb-3">${blog.subTitle}</p>
              <div class="d-flex align-items-center">
                <img
                  class="rounded-circle"
                  src="${pageContext.request.contextPath}/img/demo/avatar2.jpg"
                  width="70"
                />
                <small class="ml-2"
                  >Author
                  <span class="text-muted d-block"
                    >${blog.publishAt} &middot; 5 min. read</span
                  >
                </small>
              </div>
            </div>
            <div class="col-md-6 pr-0">
              <img
                src="${pageContext.request.contextPath}/img/demo/intro.jpg"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- End Header -->

    <!--------------------------------------
MAIN
--------------------------------------->
    <div class="container pt-4 pb-4">
      <div class="row justify-content-center">
        <div class="col-lg-2 pr-4 mb-4 col-md-12">
          <div class="sticky-top text-center">
            <div class="text-muted">Share this</div>
            <div class="share d-inline-block">
              <!-- AddToAny BEGIN -->
              <div class="a2a_kit a2a_kit_size_32 a2a_default_style">
                <a class="a2a_dd" href="https://www.addtoany.com/share"></a>
                <a class="a2a_button_facebook"></a>
                <a class="a2a_button_twitter"></a>
              </div>
              <script
                async
                src="https://static.addtoany.com/menu/page.js"
              ></script>
              <!-- AddToAny END -->
            </div>
          </div>
        </div>
        <div class="col-md-12 col-lg-8">
          <article class="article-post">
            <p>
              Holy grail funding non-disclosure agreement advisor ramen
              bootstrapping ecosystem. Beta crowdfunding iteration assets
              business plan paradigm shift stealth mass market seed money
              rockstar niche market marketing buzz market.
            </p>
            <p>
              Burn rate release facebook termsheet equity technology.
              Interaction design rockstar network effects handshake creative
              startup direct mailing. Technology influencer direct mailing
              deployment return on investment seed round.
            </p>
            <p>
              Termsheet business model canvas user experience churn rate low
              hanging fruit backing iteration buyer seed money. Virality release
              launch party channels validation learning curve paradigm shift
              hypotheses conversion. Stealth leverage freemium venture startup
              business-to-business accelerator market.
            </p>
            <p>
              Freemium non-disclosure agreement lean startup bootstrapping holy
              grail ramen MVP iteration accelerator. Strategy market ramen
              leverage paradigm shift seed round entrepreneur crowdfunding
              social proof angel investor partner network virality.
            </p>
          </article>
          <div class="border p-5 bg-lightblue">
            <div class="row justify-content-between">
              <div class="col-md-5 mb-2 mb-md-0">
                <h5 class="font-weight-bold secondfont">Become a member</h5>
                Get the latest news right in your inbox. We never spam!
              </div>
              <div class="col-md-7">
                <div class="row">
                  <div class="col-md-12">
                    <input
                      type="text"
                      class="form-control"
                      placeholder="Enter your e-mail address"
                    />
                  </div>
                  <div class="col-md-12 mt-2">
                    <button type="submit" class="btn btn-success btn-block">
                      Subscribe
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <%-- READ NEXT Section   --%>
    <div class="container pt-4 pb-4">
      <h5 class="font-weight-bold spanborder"><span>Read next</span></h5>
      <div class="row">
        <div class="col-lg-6">
          <div class="card border-0 mb-4 box-shadow h-xl-300">
            <div style="background-image: url(./assets/img/demo/3.jpg); height: 150px; background-size: cover; background-repeat: no-repeat;">
            </div>
            <div class="card-body px-0 pb-0 d-flex flex-column align-items-start">
              <h2 class="h4 font-weight-bold">
                <a class="text-dark" href="#">
                  ${nextBlogs[0].title}
                </a>
              </h2>
              <p class="card-text">
                ${nextBlogs[0].subTitle}
              </p>
              <div>
                <small class="d-block">
                  <a class="text-muted" href="./author.html">
                    Author
                  </a>
                </small>
                <small class="text-muted">
                  ${nextBlogs[0].publishAt}
                </small>
              </div>
            </div>
          </div>
        </div>
        <div class="col-lg-6">
          <div class="flex-md-row mb-4 box-shadow h-xl-300">
            <c:forEach var="blog" items="${nextBlogs}" begin="1">
              <div class="mb-3 d-flex align-items-center">
                <img height="80" src="${pageContext.request.contextPath}/img/demo/blog4.jpg"/>
                <div class="pl-3">
                  <h2 class="mb-2 h6 font-weight-bold">
                    <a class="text-dark" href="./article.html">
                        ${blog.title}
                    </a>
                  </h2>
                  <div class="card-text text-muted small">
                      ${blog.subTitle}
                  </div>
                  <small class="text-muted">${blog.publishAt}</small>
                </div>
              </div>
            </c:forEach>
          </div>
        </div>
      </div>
    </div>
    <!-- End Main -->

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
          <div>
            Made with
            <a
              target="_blank"
              class="text-secondary font-weight-bold"
              href="https://www.wowthemes.net/mundana-free-html-bootstrap-template/"
              >Mundana Theme</a
            >
            by WowThemes.net.
          </div>
        </div>
      </footer>
    </div>
    <!-- End Footer -->

    <!--------------------------------------
JAVASCRIPTS
--------------------------------------->
    <script
      src="./assets/js/vendor/jquery.min.js"
      type="text/javascript"
    ></script>
    <script
      src="./assets/js/vendor/popper.min.js"
      type="text/javascript"
    ></script>
    <script
      src="./assets/js/vendor/bootstrap.min.js"
      type="text/javascript"
    ></script>
    <script src="./assets/js/functions.js" type="text/javascript"></script>
  </body>
</html>
