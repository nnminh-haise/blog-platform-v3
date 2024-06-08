<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
    <script type="text/javascript">
        var message = ${message};
        alert(message);
    </script>
    <!-- partial:../../partials/_navbar.html -->
    <nav class="navbar default-layout-navbar col-lg-12 col-12 p-0 fixed-top d-flex flex-row">
        <div class="text-center navbar-brand-wrapper d-flex align-items-center justify-content-center">
            <a class="navbar-brand brand-logo" href="${pageContext.request.contextPath}/index.htm"><img src="images/blogg.png" alt="logo" /></a>
            <a class="navbar-brand brand-logo-mini" href="${pageContext.request.contextPath}/index.htm"><img src="images/logo-mini.svg" alt="logo" /></a>
        </div>
        <div class="navbar-menu-wrapper d-flex align-items-stretch">

        </div>
    </nav>
    <!-- partial -->
    <div class="container-fluid page-body-wrapper">
        <!-- partial:../../partials/_sidebar.html -->
        <nav class="sidebar sidebar-offcanvas" id="sidebar">
            <ul class="nav">
                <li class="nav-item nav-profile">
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
                    <h3 class="page-title"> Categories </h3>
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
                                <h4 class="card-title">Category List</h4>
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th> Name </th>
                                        <th> Slug </th>
                                        <th> Manage </th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach var="category" items="${categories}" varStatus="loop">
                                        <c:set var="rowClass" value=""/>
                                        <c:choose>
                                            <c:when test="${loop.index % 5 == 0}">
                                                <c:set var="rowClass" value="table-info"/>
                                            </c:when>
                                            <c:when test="${loop.index % 5 == 1}">
                                                <c:set var="rowClass" value="table-warning"/>
                                            </c:when>
                                            <c:when test="${loop.index % 5 == 2}">
                                                <c:set var="rowClass" value="table-danger"/>
                                            </c:when>
                                            <c:when test="${loop.index % 5 == 3}">
                                                <c:set var="rowClass" value="table-success"/>
                                            </c:when>
                                            <c:when test="${loop.index % 5 == 4}">
                                                <c:set var="rowClass" value="table-primary"/>
                                            </c:when>
                                        </c:choose>
                                        <tr class="${rowClass}">
                                            <td><c:out value="${category.name}" /></td>
                                            <td><c:out value="${category.slug}" /></td>
                                            <td style="width: 20%;">
                                                <a href="${pageContext.request.contextPath}/admin/categories/edit/${category.slug}.htm">
                                                    <button type="button" class="btn btn-gradient-warning btn-fw">Edit</button>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/admin/categories/edit/${category.slug}.htm" onclick="return confirm('are you sure')">
                                                    <button type="button" class="btn btn-gradient-danger btn-fw">Delete</button>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>

                                <div style="margin:30px; text-align : right">
                                    <a href="category/index.htm?pageCategory=${pageCategory-1}">

                                        <button type="button"  ${pageCategory == 1 ? 'disabled' : ''}> < </button>
                                    </a>
                                    Page ${pageCategory} of ${totalPages}
                                    <a href="category/index.htm?pageCategory=${pageCategory+1}">

                                        <button type="button"  ${pageCategory == totalPages ? 'disabled' : ''}> > </button>
                                    </a>
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
<!-- endinject -->
<!-- Custom js for this page -->
<!-- End custom js for this page -->
</body>
</html>