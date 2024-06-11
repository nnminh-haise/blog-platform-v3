<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">
<head>
  <meta charset="utf-8" />
  <meta name="viewport"
        content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
  <title>
    Blog Platform
  </title>
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/vendors/mdi/css/materialdesignicons.min.css"/>
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/vendors/css/vendor.bundle.base.css"/>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/style.css"/>
  <link rel="shortcut icon"
        href="${pageContext.request.contextPath}/images/favicon.ico"/>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <style>
    .btn-gradient-green {
      background: linear-gradient(to right, #84d9d2, #07cdae) !important;
      color: white;
    }
  </style>
</head>
<body>
<div class="container-scroller">
  <nav class="navbar default-layout-navbar col-lg-12 col-12 p-0 fixed-top d-flex flex-row">
    <div class="text-center navbar-brand-wrapper d-flex align-items-center justify-content-center">
      <a class="navbar-brand brand-logo" href="${pageContext.request.contextPath}/index.htm">
        <img src="${pageContext.request.contextPath}/images/blogg.png" alt="logo"/>
      </a>
      <a class="navbar-brand brand-logo-mini" href="${pageContext.request.contextPath}/index.htm">
        <img src="${pageContext.request.contextPath}/images/logo-mini.svg" alt="logo"/>
      </a>
    </div>
  </nav>

  <div class="container-fluid page-body-wrapper">
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
        <li class="nav-item link-item">
          <a  class="nav-link" href="${pageContext.request.contextPath}/admin/index.htm">
            <span style="color: white" class="menu-title">Blogs</span>
            <i class="mdi mdi-contacts menu-icon"></i>
          </a>
        </li>
        <li class="nav-item link-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/admin/categories/index.htm">
            <span style="color: black" class="menu-title">Categories</span>
            <i class="mdi mdi-format-list-bulleted menu-icon"></i>
          </a>
        </li>
      </ul>
    </nav>

    <div class="main-panel">
      <div class="content-wrapper">
        <%-- Heading --%>
        <h1 class="page-title mb-4" style="font-size: 1.5rem !important; font-weight: 600 !important;">Blog Gallery</h1>
        <%-- Header for gallery --%>
        <div class="page-header" >
          <div class="navbar-menu-wrapper d-flex align-items-stretch">
            <div class="btn-group">
              <div class="form-group" style="display: flex;
                    align-items: center;
                    justify-content: center;
                    gap: 0.5rem;
                     margin-bottom: 0 !important;">
                <label for="category-filter" style="margin-bottom: 0 !important;">Categories</label>
                <select class="form-control" id="category-filter" name="filterOptionsForCategory">
                  <option value="null">All categories</option>
                  <c:forEach var="category" items="${categoryListForFilterOptions}">
                    <option value="${category.slug}">${category.name}</option>
                  </c:forEach>
                </select>
              </div>
            </div>
          </div>
          <%-- Sorting Section --%>
          <div class="form-group row" style="margin-bottom: 0 !important;">
            <label class="col-sm-6 col-form-label" style="margin-bottom: 0 !important;">
              Order By: Create Date
            </label>
            <div class="col-sm-3">
              <div class="form-check">
                <label class="form-check-label">
                  <input type="radio"
                         class="form-check-input"
                         name="sortingOptions"
                         id="membershipRadios1"
                         value="asc"
                         onclick="handleSortingOptionChange(this.value)"/>
                  <i class="input-helper">
                    ASC
                  </i>
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
                  <i class="input-helper">
                    DESC
                  </i>
                </label>
              </div>
            </div>
          </div>
          <%-- Create new blog section --%>
          <div class="template-demo">
            <a href="${pageContext.request.contextPath}/admin/insert.htm">
              <button type="button" class="btn btn-gradient-success btn-fw">
                New Blog
              </button>
            </a>
          </div>
        </div>

        <div class="row">
          <div class="col-lg-12 grid-margin stretch-card">
            <div class="card">
              <div class="card-body">
                <table class="table table-dark">
                  <thead>
                  <tr>
                    <th>Title</th>
                    <th>Created at</th>
                    <th>Updated at</th>
                    <th style="display: flex; justify-content: center; align-items: center">Manage</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="blog" items="${blogList}" varStatus="loop">
                    <tr>
                      <td style="overflow: hidden !important;
                      text-overflow: ellipsis !important;
                      white-space: nowrap !important;
                      max-width: 322px;
                      vertical-align: middle;
                      font-size: 0.875rem;
                      line-height: 1;
                      padding: 0.9375rem;
                      ">
                        ${blog.title}
                      </td>
                      <td>
                        <fmt:formatDate value="${blog.createAtAsDate()}" pattern="MMMM d, yyyy HH:mm:ss" var="formattedCreateDate" />
                        <c:out value="${formattedCreateDate}" />
                      </td>
                      <td>
                        <fmt:formatDate value="${blog.updateAtAsDate()}" pattern="MMMM d, yyyy HH:mm:ss" var="formattedUpdateDate" />
                        <c:out value="${formattedUpdateDate}" />
                      </td>
                      <td style="width: 20%">
                        <a href="${pageContext.request.contextPath}/admin/edit/${blog.slug}.htm">
                          <button type="button" class="btn btn-dark btn-fw" style="min-width: 110px; background: #00cdf6;">Edit</button>
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/remove/${blog.slug}.htm" onclick="return confirm('are you sure')">
                          <button type="button" class="btn btn-dark btn-fw" style="min-width: 110px; background: #ff3333">Delete</button>
                        </a>
                      </td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>

                <%-- Pagination elements --%>
                <c:if test="${numberOfPage > 1}">
                  <div class="pagination-container" style="
                        margin: 30px;
                        text-align: right;
                        top: 0;
                        right: 100%;
                      ">
                    <button type="button" onclick="handlePageLeftButton()">&#10094;</button>
                    Page ${currentPage + 1} of ${numberOfPage}
                    <button type="button" onclick="handlePageRightButton()">&#10095;</button>
                  </div>
                </c:if>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/vendors/js/vendor.bundle.base.js"></script>
<script src="${pageContext.request.contextPath}/js/off-canvas.js"></script>
<script src="${pageContext.request.contextPath}/js/hoverable-collapse.js"></script>
<script src="${pageContext.request.contextPath}/js/misc.js"></script>

<%-- Pagination logic --%>
<script type="text/javascript">
    function retrieveInformation() {
        return {
            url: "${pageContext.request.contextPath}/admin/index.htm",
            currentPage: +"${currentPage}",
            currentSize: +"${currentSize}",
            numberOfPage: +"${numberOfPage}",
            orderBy: "${orderBy}",
            slug: "${slug}"
        };
    }

    function handlePageLeftButton() {
        const data = retrieveInformation();
        const newPage = (data.currentPage === 0 ? data.numberOfPage - 1 : data.currentPage - 1);
        let request = data.url + "?page=" + newPage + "&size=" + data.currentSize + "&orderBy=" + data.orderBy;
        if (data.slug !== "" && data.slug !== "null") {
            request += "&slug=" + data.slug;
        }
        window.location.href = request;
    }

    function handlePageRightButton() {
        const data = retrieveInformation();
        const newPage = (+data.currentPage === data.numberOfPage - 1 ? 0 : data.currentPage + 1);
        let request = data.url + "?page=" + newPage + "&size=" + data.currentSize + "&orderBy=" + data.orderBy;
        if (data.slug !== "" && data.slug !== "null") {
            request += "&slug=" + data.slug;
        }
        window.location.href = request;
    }

    function handleSortingOptionChange(orderBy) {
        const data = retrieveInformation();
        let request = data.url + "?page=" + data.currentPage + "&size=" + data.currentSize + "&orderBy=" + orderBy;
        if (data.slug !== "" && data.slug !== "null") {
            request += "&slug=" + data.slug;
        }
        window.location.href = request;
    }

  document.addEventListener("DOMContentLoaded", function() {
      const data = retrieveInformation();
      const radioButtons = document.querySelectorAll('input[name="sortingOptions"]');

      radioButtons.forEach(radio => {
          if (radio.value === data.orderBy) {
              radio.checked = true;
          }
      });

      const selections = document.getElementById("category-filter");
      selections.addEventListener("change", () => {
          let url = data.url + "?page=0&size=5&orderBy=asc";
          if (selections.value !== "null") {
              url = url + "&slug=" + selections.value;
          }
          window.location.href = url;
      })

      selections.value = (data.slug === "" ? "null" : data.slug);
  });
</script>

<script>
  $(document).ready(function() {
    if (window.location.pathname.includes('/admin/index.htm')) {
      $('.link-item:first').addClass('btn-gradient-green');
    } else {
      $('.link-item-item').not(':first').css('background-color', '#00F66BFF'); // Highlight color for other li elements
    }
  });
</script>
</body>
</html>
