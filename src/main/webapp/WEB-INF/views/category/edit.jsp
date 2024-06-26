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
  <title>${selectingCategory.name} Details</title>
  <!-- plugins:css -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/vendors/mdi/css/materialdesignicons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/vendors/css/vendor.bundle.base.css">
  <!-- Layout styles -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <!-- End layout styles -->
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/images/favicon.ico" />
  <style>
    .btn-gradient-green {
      background: linear-gradient(to right, #84d9d2, #07cdae) !important;
      color: white;
    }
  </style>
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
        <style>
          .btn-gradient-green {
            background: linear-gradient(to right, #84d9d2, #07cdae) !important;
            color: white;
          }
        </style>
        <li class="nav-item">
          <a  class="nav-link" href="${pageContext.request.contextPath}/admin/index.htm">
            <span style="color: black"  class="menu-title">Blogs</span>
            <i class="mdi mdi-contacts menu-icon"></i>
          </a>
        </li>
        <li class="nav-item btn-gradient-green">
          <a class="nav-link" href="${pageContext.request.contextPath}/admin/categories/index.htm">
            <span style="color: white" class="menu-title">Categories</span>
            <i class="mdi mdi-format-list-bulleted menu-icon"></i>
          </a>
        </li>
      </ul>
    </nav>
    <!-- partial -->
    <div class="main-panel">
      <div class="content-wrapper">
        <div class="page-header">
          <h3 class="page-title">
            Category: <strong>${selectingCategory.name}</strong>
          </h3>
        </div>
        <div class="row">
          <div class="col-lg-12 stretch-card">
            <div class="card">
              <div class="card-body">
                <div class="form-group">
                  <form:form action="${pageContext.request.contextPath}/admin/categories/edit/${selectingCategory.slug}.htm"
                             method="POST"
                             modelAttribute="updateCategoryDto">
                    <label for="exampleInputName1">Category's name</label>
                    <form:input type="text" path ="name"  class="form-control" id="exampleInputName1" placeholder="Category's name" />
                    <div style="display: flex; gap: 1rem; align-items: center " class="mt-4">
                      <button type="submit" class="btn btn-gradient-primary ">Save</button>
                        <c:if test="${relatedBlogs.size() == 0}">
<%--                          TODO: fix this weird bug where putting a button tag inside an a tag does not work--%>
                          <a href="${pageContext.request.contextPath}/admin/categories/remove/${selectingCategory.slug}.htm" class="btn btn-gradient-danger me-2">
<%--                            <button id="removeCategoryButton" class="btn btn-gradient-danger me-2" onclick="handleRemoveCategoryButton()">--%>
                              Remove
<%--                            </button>--%>
                          </a>
                        </c:if>
                    </div>
                  </form:form>
                </div>
                <div class="form-group">
                  <c:if test="${relatedBlogs.size() > 0}">
                    <label for="relatedBlogTable">Related Blogs</label>
                    <table class="table table-dark" id="relatedBlogTable">
                      <thead>
                      <tr>
                        <th>Title</th>
                        <th>Created at</th>
                        <th>Updated at</th>
                        <th style="display: flex; justify-content: center; align-items: center">Manage</th>
                      </tr>
                      </thead>
                      <c:forEach var="blog" items="${relatedBlogs}">
                        <tr>
                          <td>${blog.title}</td>
                          <td>
                            <fmt:formatDate value="${blog.createAtAsDate()}" pattern="MMMM d, yyyy HH:mm:ss" var="formattedCreateDate" />
                            <c:out value="${formattedCreateDate}" />
                          </td>
                          <td>
                            <fmt:formatDate value="${blog.updateAtAsDate()}" pattern="MMMM d, yyyy HH:mm:ss" var="formattedUpdateDate" />
                            <c:out value="${formattedUpdateDate}" />
                          </td>
                          <td style="...">
                            <a href="${pageContext.request.contextPath}/admin/edit/${blog.slug}.htm">
                              <button type="button" class="btn btn-dark btn-fw" style="min-width: 110px; background: #00cdf6;">
                                Edit Blog
                              </button>
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/categories/delete.htm?blogSlug=${blog.slug}&categorySlug=${selectingCategory.slug}">
                              <button type="button" class="btn btn-dark btn-fw" style="min-width: 110px; background: #00cdf6;">
                                Remove from category
                              </button>
                            </a>
                          </td>
                        </tr>
                      </c:forEach>
                    </table>
                    <c:if test="${totalNumberOfPage > 1}">
                      <div class="pagination-container" style="
                        margin: 30px;
                        text-align: right;
                        top: 0;
                        right: 100%;">
                        <button type="button" onclick="handlePageLeftButton()">&#10094;</button>
                        Page ${currentPage + 1} of ${totalNumberOfPage}
                        <button type="button" onclick="handlePageRightButton()">&#10095;</button>
                      </div>
                    </c:if>
                  </c:if>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- content-wrapper ends -->
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
<script type="text/javascript">
    function handlePageLeftButton() {
        const baseUrl = "${pageContext.request.contextPath}/admin/categories/edit/" + "${selectingCategory.slug}" + ".htm";
        const page = "${currentPage}";
        const size = "${currentSize}";
        const totalNumberOfPage = "${totalNumberOfPage}";
        const orderBy = "${orderBy}";

        const newPage = (+page === 0 ? totalNumberOfPage - 1 : +page - 1);
        const url = baseUrl + "?page=" + newPage + "&size=" + size;
        window.location.href = url;
    }

    function handlePageRightButton() {
        const baseUrl = "${pageContext.request.contextPath}/admin/categories/edit/" + "${selectingCategory.slug}" + ".htm";
        const page = "${currentPage}";
        const size = "${currentSize}";
        const totalNumberOfPage = "${totalNumberOfPage}";
        const orderBy = "${orderBy}";

        const newPage = (+page === totalNumberOfPage - 1 ? 0 : +page + 1);
        const url = baseUrl + "?page=" + newPage + "&size=" + size;
        window.location.href = url;
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    const alertMessage = "${alertMessage}";
    console.log("alert:", alertMessage);
    if (alertMessage.length > 0) {
        Swal.fire({
            title: 'Blog Platform CMS Message',
            text: alertMessage,
            icon: 'info',
            confirmButtonText: 'OK',
            background: '#EEF5FF',
            customClass: {
                popup: 'custom-popup-class',
                title: 'custom-title-class',
                confirmButton: 'custom-confirm-button-class'
            }
        });
    }
</script>
<!-- endinject -->
<!-- Custom js for this page -->
<!-- End custom js for this page -->
</body>
</html>