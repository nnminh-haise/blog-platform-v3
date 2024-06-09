<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en" >
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
<c:if test="${not empty message}">
    <script type="text/javascript">
        /* Gán biến message từ JSP */
        var message = "${message}";
        alert(message);
    </script>
    <p>${message}</p>
</c:if>
<div class="container-scroller">
    <!-- partial:../../partials/_navbar.html -->
    <nav class="navbar default-layout-navbar col-lg-12 col-12 p-0 fixed-top d-flex flex-row">
        <div class="text-center navbar-brand-wrapper d-flex align-items-center justify-content-center">
            <a class="navbar-brand brand-logo" href="${pageContext.request.contextPath}/index.htm"><img src="${pageContext.request.contextPath}/images/blogg.png" alt="logo" /></a>
            <a class="navbar-brand brand-logo-mini" href="${pageContext.request.contextPath}/index.htm"><img src="${pageContext.request.contextPath}/images/logo-mini.svg" alt="logo" /></a>
        </div>
        <div class="navbar-menu-wrapper d-flex align-items-stretch">
            <button class="navbar-toggler navbar-toggler-right d-lg-none align-self-center" type="button" data-toggle="offcanvas">
                <span class="mdi mdi-menu"></span>
            </button>
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
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/blogs/index.htm">
                        <span class="menu-title">Icons</span>
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
                    <h3 class="page-title"> Categories </h3>
                    <div class="template-demo">
                        <a href="/admin/categories/insert.htm" >
                            <button type="button" class="btn btn-gradient-success btn-fw">Add</button>
                        </a>

                    </div>
                </div>
                <div class="row">
                    <div class="col-12 grid-margin stretch-card">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">Add new Category</h4>
<%--                                <p class="card-description"> Fill information in the blank </p>--%>
                                <form:form class="form-inline" method="post" action="${pageContext.request.contextPath}/admin/categories/insert.htm" modelAttribute="createCategoryDto">
                                    <div class="form-group">
                                        <label  class="sr-only">Name of Category</label>
                                        <form:input  class="form-control mb-2 mr-sm-2" path="name" placeholder="Category's name"/>
                                    </div>
                                    <button type="submit" class="btn btn-gradient-primary mb-2">Create</button>
                                </form:form>
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