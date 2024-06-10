<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <!-- Required meta tags -->
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Purple Admin</title>
  <!-- plugins:css -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/vendors/mdi/css/materialdesignicons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/vendors/css/vendor.bundle.base.css">
  <!-- endinject -->
  <!-- Plugin css for this page -->
  <!-- End plugin css for this page -->
  <!-- inject:css -->
  <!-- endinject -->
  <!-- Layout styles -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <!-- End layout styles -->
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/images/favicon.ico" />
</head>
<body>
<div class="container-scroller">
  <!-- partial:../../partials/_navbar.html -->
  <nav class="navbar default-layout-navbar col-lg-12 col-12 p-0 fixed-top d-flex flex-row">
    <div class="text-center navbar-brand-wrapper d-flex align-items-center justify-content-center">
      <a class="navbar-brand brand-logo" href="${pageContext.request.contextPath}/index.htm">
        <img src="${pageContext.request.contextPath}/images/blogg.png" alt="logo" />
      </a>
      <a class="navbar-brand brand-logo-mini" href="${pageContext.request.contextPath}/index.htm">
        <img src="${pageContext.request.contextPath}/images/logo-mini.svg" alt="logo" />
      </a>
    </div>
  </nav>
  <!-- partial -->
  <div class="container-fluid page-body-wrapper">
    <!-- partial:../../partials/_sidebar.html -->
    <nav class="sidebar sidebar-offcanvas" id="sidebar">
      <ul class="nav">
        <li class="nav-item nav-profile">
          <a href="${pageContext.request.contextPath}/index.htm" class="nav-link">
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
          <h3 class="page-title">Categories</h3>
          <div class="template-demo">
            <a href="${pageContext.request.contextPath}/admin/categories/insert.htm" >
              <button type="button" class="btn btn-gradient-success btn-fw">Add</button>
            </a>
          </div>
        </div>
        <div class="row">
          <div class="col-lg-12 stretch-card">
            <div class="card">
              <div class="card-body">
                <table class="table table-bordered">
                  <thead>
                  <tr>
                    <th>Name</th>
                    <th>Create At</th>
                    <th>Update At</th>
                    <th>Manage</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="category" items="${categories}">
                    <tr>
                      <td>${category.name}</td>
                      <td>
                        <fmt:formatDate value="${category.createAtAsDate()}" pattern="MMMM d, yyyy HH:mm:ss" var="formattedCreateDate" />
                        <c:out value="${formattedCreateDate}"/>
                      </td>
                      <td>
                        <fmt:formatDate value="${category.updateAtAsDate()}" pattern="MMMM d, yyyy HH:mm:ss" var="formattedUpdateDate" />
                        <c:out value="${formattedUpdateDate}"/>
                      </td>
                      <td style="width: 20%;">
                        <a href="${pageContext.request.contextPath}/admin/categories/edit/${category.slug}.htm">
                          <button type="button" class="btn btn-gradient-warning btn-fw">Edit</button>
                        </a>
<%--                        <a href="${pageContext.request.contextPath}/admin/categories/delete/${category.slug}.htm" onclick="return confirm('are you sure')">--%>
<%--                          <button type="button" class="btn btn-gradient-danger btn-fw">Delete</button>--%>
<%--                        </a>--%>
                      </td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
                <div class="pagination-container" style="
                        margin: 30px;
                        text-align: right;
                        top: 0;
                        right: 100%;">
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
      <footer class="footer">

      </footer>
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
<script src="js/file-upload.js"></script><%-- Pagination logic --%>
<script type="text/javascript">
    const baseUrl = "${pageContext.request.contextPath}/admin/categories/index.htm";
    const page = "${currentPage}";
    const size = "${currentSize}";
    const totalNumberOfPage = "${totalNumberOfPage}";
    const orderBy = "${orderBy}";

    function handlePageLeftButton() {
        const newPage = (+page === 0 ? totalNumberOfPage - 1 : +page - 1);
        window.location.href = baseUrl + "?page=" + newPage + "&size=" + size;
    }

    function handlePageRightButton() {
        const newPage = (+page === totalNumberOfPage - 1 ? 0 : +page + 1);
        window.location.href = baseUrl + "?page=" + newPage + "&size=" + size;
    }
</script>
<!-- endinject -->
<!-- Custom js for this page -->
<!-- End custom js for this page -->
</body>
</html>