<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
      <a class="navbar-brand brand-logo" href="${pageContext.request.contextPath}/index.html"
      ><img src="${pageContext.request.contextPath}/images/blogg.png" alt="logo"
      /></a>
      <a class="navbar-brand brand-logo-mini" href="${pageContext.request.contextPath}/index.html"
      ><img src="${pageContext.request.contextPath}/images/logo-mini.svg" alt="logo"
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
        <li class="nav-item nav-profile" id="${adminInformation.sub}">
          <a href="#" class="nav-link">
            <div class="nav-profile-image">
              <img src="${adminInformation.picture}" alt="profile"/>
            </div>
            <div class="nav-profile-text d-flex flex-column">
              <span class="font-weight-bold mb-2">${adminInformation.name}</span>
              <span class="text-secondary text-small">${adminInformation.email}</span>
            </div>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/admin/index.htm">
            <span class="menu-title">Blogs</span>
            <i class="mdi mdi-contacts menu-icon"></i>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/admin/categories/index.htm">
            <span class="menu-title">Categories</span>
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

          <%-- Order by section --%>
          <div class="form-group row">
            <label class="col-sm-6 col-form-label">Order By: Create Timestamp</label>
            <div class="col-sm-3">
              <div class="form-check">
                <label class="form-check-label">
                  <input type="radio"
                         class="form-check-input"
                         name="sortingOptions"
                         id="membershipRadios1"
                         value="asc"
                         onclick="handleSortingOptionChange(this.value)" />
                  ASC <i class="input-helper"></i>
                </label>
              </div>
            </div>
            <div class="col-sm-3">
              <div class="form-check">
                <label class="form-check-label">
                  <input type="radio"
                         class="form-check-input"
                         name="sortingOptions"
                         id="membershipRadios2"
                         value="desc"
                         onclick="handleSortingOptionChange(this.value)"/>
                  DESC <i class="input-helper"></i>
                </label>
              </div>
            </div>
          </div>

          <div class="template-demo">
            <a href="${pageContext.request.contextPath}/admin/insert.htm">
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
                    <th>Published</th>
                    <th>Created at</th>
                    <th>Updated at</th>
                    <th>Manage</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach
                          var="blog"
                          items="${blogList}"
                          varStatus="loop"
                  >
                    <tr>
                      <td><c:out value="${blog.title}" /></td>
                      <c:choose>
                        <c:when test="${blog.publishAt == null}">
                          <td>
                            <p>Pick a publish date</p>
                          </td>
                        </c:when>
                        <c:when test="${blog.publishAt != null}">
                          <td>
                            <fmt:formatDate value="${blog.publishDateAsDate()}" pattern="MMMM d, yyyy" var="formattedPublishDate" />
                            <c:out value="${formattedPublishDate}" />
                          </td>
                        </c:when>
                      </c:choose>
                      <td>
                        <fmt:formatDate value="${blog.createAtAsDate()}" pattern="MMMM d, yyyy HH:mm:ss" var="formattedCreateDate" />
                        <c:out value="${formattedCreateDate}" />
                      </td>
                      <td>
                        <fmt:formatDate value="${blog.updateAtAsDate()}" pattern="MMMM d, yyyy HH:mm:ss" var="formattedUpdateDate" />
                        <c:out value="${formattedUpdateDate}" />
                      </td>
                      <td style="width: 20%">
                        <a
                                href="${pageContext.request.contextPath}/admin/edit/${blog.slug}.htm"
                        >
                          <button
                                  type="button"
                                  class="btn btn-dark btn-fw"
                          >
                            Edit
                          </button>
                        </a>
                        <a
                                href="${pageContext.request.contextPath}/admin/edit/${blog.slug}.htm"
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
                <%-- Pagination elements --%>
                <div class="pagination-container" style="
                        margin: 30px;
                        text-align: right;
                        top: 0;
                        right: 100%;
                      ">
                  <button type="button" onclick="handlePageLeftButton()">&#10094;</button>
                  Page ${currentPage + 1} of ${totalNumberOfPage}
                  <button type="button" onclick="handlePageRightButton()">&#10095;</button>
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
<script src="${pageContext.request.contextPath}/vendors/js/vendor.bundle.base.js"></script>
<!-- endinject -->
<!-- Plugin js for this page -->
<!-- End plugin js for this page -->
<!-- inject:js -->
<script src="${pageContext.request.contextPath}/js/off-canvas.js"></script>
<script src="${pageContext.request.contextPath}/js/hoverable-collapse.js"></script>
<script src="${pageContext.request.contextPath}/js/misc.js"></script>
<%-- Pagination logic --%>
<script type="text/javascript">
  const baseUrl = "${pageContext.request.contextPath}/admin/index.htm";
  const page = "${currentPage}";
  const size = "${currentSize}";
  const totalNumberOfPage = "${totalNumberOfPage}";
  const orderBy = "${orderBy}";
  const filteringSlug = "${filterBySlug}";

  function handlePageLeftButton() {
    const newPage = (+page === 0 ? totalNumberOfPage - 1 : +page - 1);
    window.location.href = baseUrl + "?page=" + newPage + "&size=" + size;
  }

  function handlePageRightButton() {
    const newPage = (+page === totalNumberOfPage - 1 ? 0 : +page + 1);
    window.location.href = baseUrl + "?page=" + newPage + "&size=" + size;
  }

  function handleSortingOptionChange(orderBy) {
    const url = baseUrl + "?page=" + (+page) + "&size=" + (size) + "&orderBy=" + orderBy;
    window.location.href = url;
  }

  document.addEventListener("DOMContentLoaded", function() {
      const radioButtons = document.querySelectorAll('input[name="sortingOptions"]');

      radioButtons.forEach(radio => {
          radio.addEventListener('change', function() {
              if (this.value() === orderBy) {
                  handleSortingOptionChange(this.value);
              }
          });
      });
  });
</script>
<!-- endinject -->

<!-- Custom js for this page -->
<!-- End custom js for this page -->
</body>
</html>