<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <title>Blog Platform</title>
    <!-- plugins:css -->
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/vendors/mdi/css/materialdesignicons.min.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/vendors/css/vendor.bundle.base.css"
    />
    <!-- endinject -->
    <!-- Plugin css for this page -->
    <!-- End plugin css for this page -->
    <!-- inject:css -->
    <!-- endinject -->
    <!-- Layout styles -->
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css"
    />
    <!-- End layout styles -->
    <link
      rel="shortcut icon"
      href="${pageContext.request.contextPath}/images/favicon.ico"
    />
  </head>
  <body>
    <div class="container-scroller">
      <!-- partial:../../partials/_navbar.html -->
      <nav class="navbar default-layout-navbar col-lg-12 col-12 p-0 fixed-top d-flex flex-row">
        <div class="text-center navbar-brand-wrapper d-flex align-items-center justify-content-center">
          <a class="navbar-brand brand-logo" href="../../index.html"
            ><img src="images/blogg.png" alt="logo"
          /></a>
          <a class="navbar-brand brand-logo-mini" href="../../index.html"
            ><img src="images/logo-mini.svg" alt="logo"
          /></a>
        </div>
        <div class="navbar-menu-wrapper d-flex align-items-stretch">
          <div class="btn-group">
            <div class="form-group">
              <%-- Category nav bar --%>
              <label for="category-filter">Categories</label>
              <select class="form-control" id="category-filter" name="slug">
                <option value="null">All categories</option>
                <c:forEach var="category" items="${categories}">
                  <option value="${category.slug}">${category.name}</option>
                </c:forEach>
              </select>
            </div>
          </div>
        </div>
      </nav>
      <!-- partial -->
      <div class="container-fluid page-body-wrapper">
        <!-- partial:../../partials/_sidebar.html -->
        <nav class="sidebar sidebar-offcanvas" id="sidebar">
          <ul class="nav">
            <li class="nav-item nav-profile" id="${userInformation.sub}">
              <a href="#" class="nav-link">
                <div class="nav-profile-image">
                  <img src="${userInformation.picture}" alt="profile"/>
                </div>
                <div class="nav-profile-text d-flex flex-column">
                  <span class="font-weight-bold mb-2">${userInformation.name}</span>
                  <span class="text-secondary text-small">${userInformation.email}</span>
                </div>
                <i class="mdi mdi-bookmark-check text-success nav-profile-badge"></i>
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="">
                <span class="menu-title">Blogs</span>
                <i class="mdi mdi-contacts menu-icon"></i>
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="">
                <span class="menu-title">Category</span>
                <i class="mdi mdi-format-list-bulleted menu-icon"></i>
              </a>
            </li>
          </ul>
        </nav>
        <!-- partial -->
        <div class="main-panel">
          <div class="content-wrapper">
            <div class="page-header">
              <h3 class="page-title">Blogs</h3>

              <div class="form-group row">
                <label class="col-sm-6 col-form-label"
                  >Order By: Published</label
                >
                <div class="col-sm-3">
                  <div class="form-check">
                    <label class="form-check-label">
                      <input
                        type="radio"
                        class="form-check-input"
                        name="orderBy"
                        id="membershipRadios1"
                        value="ASC"
                        checked="" />
                      ASC <i class="input-helper"></i
                    ></label>
                  </div>
                </div>
                <div class="col-sm-3">
                  <div class="form-check">
                    <label class="form-check-label">
                      <input
                        type="radio"
                        class="form-check-input"
                        name="orderBy"
                        id="membershipRadios2"
                        value="DESC" />
                      DESC <i class="input-helper"></i
                    ></label>
                  </div>
                </div>
              </div>
              <div class="template-demo">
                <a href="${pageContext.request.contextPath}/admin/insert.htm?code=${validationCode}">
                  <p>${pageContext.request.contextPath}/admin/insert.htm?code=${validationCode}</p>
                  <button type="button" class="btn btn-gradient-success btn-fw">
                    Add
                  </button>
                </a>
              </div>
            </div>
            <div class="row">
              <div class="col-lg-12 grid-margin stretch-card">
                <div class="card">
                  <div class="card-body">
                    <h4 class="card-title">Blog</h4>

                    <table class="table table-dark">
                      <thead>
                        <tr>
                          <th>Title</th>
                          <th>Slug</th>
                          <th>Published</th>
                          <th>Manage</th>
                        </tr>
                      </thead>
                      <tbody>
                        <c:forEach
                          var="blog"
                          items="${categories}"
                          varStatus="loop"
                        >
                          <tr>
                            <td><c:out value="${blog.id}" /></td>
                            <td><c:out value="${blog.name}" /></td>
                            <td><c:out value="${blog.slug}" /></td>
                            <td style="width: 20%">
                              <a
                                href="${pageContext.request.contextPath}/editor/${blog.id}.htm?pageBlog=${pageBlog}"
                              >
                                <button
                                  type="button"
                                  class="btn btn-dark btn-fw"
                                >
                                  Edit
                                </button>
                              </a>
                              <a
                                href="blogs/delete/${blog.id}.htm?pageBlog=${pageBlog}"
                                onclick="return confirm('are you sure')"
                              >
                                <button
                                  type="button"
                                  class="btn btn-dark btn-fw"
                                >
                                  Delete
                                </button>
                              </a>
                            </td>
                          </tr>
                        </c:forEach>
                      </tbody>
                    </table>

                    <div
                      class="pagination-container"
                      style="
                        margin: 30px;
                        text-align: right;
                        top: 0;
                        right: 100%;
                      "
                    >
                      <a href="category/index.htm?pageCategory=-1">
                        <button type="button">&#10094;</button>
                      </a>
                      Page of
                      <a href="category/index.htm?pageCategory=1">
                        <button type="button" disabled>&#10095;</button>
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- content-wrapper ends -->
          <!-- partial:../../partials/_footer.html -->
          <footer class="footer"></footer>
          <!-- partial -->
        </div>
        <!-- main-panel ends -->
      </div>
      <!-- page-body-wrapper ends -->
    </div>
    <!-- container-scroller -->
    <!-- plugins:js -->
    <script src="vendors/js/vendor.bundle.base.js"></script>
    <!-- endinject -->
    <!-- Plugin js for this page -->
    <!-- End plugin js for this page -->
    <!-- inject:js -->
    <script src="js/off-canvas.js"></script>
    <script src="js/hoverable-collapse.js"></script>
    <script src="js/misc.js"></script>
    <!-- endinject -->

    <!-- Custom js for this page -->
    <!-- End custom js for this page -->
  </body>
</html>
