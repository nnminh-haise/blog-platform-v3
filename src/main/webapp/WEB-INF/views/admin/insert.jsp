<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en" >
<head>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>
    Blog Manager
  </title>
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/vendors/mdi/css/materialdesignicons.min.css">
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/vendors/css/vendor.bundle.base.css">
  <link rel="stylesheet"
        href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">

  <!-- Editor.js CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/editorjs@latest"></script>

  <!-- Editor.js Header Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/header@latest"></script>

  <!-- Editor.js List Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/list@latest"></script>

  <!-- Editor.js Paragraph Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/paragraph@latest"></script>

  <!-- Editor.js Image Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/image@latest"></script>

  <!-- Editor.js Embed Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/embed@latest"></script>

  <!-- Editor.js Link Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/link@latest"></script>

  <!-- Editor.js Delimiter Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/delimiter@latest"></script>

  <!-- Editor.js Code Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/code@latest"></script>

  <!-- Editor.js CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/editorjs@latest"></script>

  <!-- Editor.js Header Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/header@latest"></script>

  <!-- Editor.js List Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/list@latest"></script>

  <!-- Editor.js Paragraph Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/paragraph@latest"></script>

  <!-- Editor.js Image Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/image@latest"></script>

  <!-- Editor.js Embed Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/embed@latest"></script>

  <!-- Editor.js Link Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/link@latest"></script>

  <!-- Editor.js Delimiter Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/delimiter@latest"></script>

  <!-- Editor.js Code Plugin CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@editorjs/code@latest"></script>
  <style>
      .checkbox-row {
          display: flex;
          flex-wrap: wrap;
      }
      .checkbox-column {
          flex: 0 0 25%; /* 25% width for 4 columns per row */
      }
      .btn-gradient-green {
        color: white;
        background: linear-gradient(to right, #84d9d2, #07cdae) !important;
      }
  </style>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/style.css">
  <link rel="shortcut icon"
        href="${pageContext.request.contextPath}/images/favicon.ico" />
</head>
<body>
<div class="container-scroller">
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

  <div class="container-fluid page-body-wrapper">
    <nav class="sidebar sidebar-offcanvas" id="sidebar">
      <ul class="nav">
        <li class="nav-item nav-profile">
          <a href="#" class="nav-link">
            <div class="nav-profile-image">
              <img src="${adminInformation.picture}" alt="profile"/>
            </div>
            <div class="nav-profile-text d-flex flex-column">
              <span class="font-weight-bold mb-2">
                ${adminInformation.name}
              </span>
              <span class="text-secondary text-small">
                ${adminInformation.email}
              </span>
            </div>
          </a>
        </li>
        <li class="nav-item btn-gradient-green">
          <a class="nav-link " href="${pageContext.request.contextPath}/admin/index.htm">
            <span style="color: white !important;" class="menu-title">Blogs</span>
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

    <div class="main-panel">
      <div class="content-wrapper">
        <div class="page-header">
          <h3 class="page-title">New Blog</h3>
        </div>
        <div class="row">
          <div class="col-12 grid-margin stretch-card">
            <div class="card">
              <div class="card-body">
                <form:form class="forms-sample"
                           enctype="multipart/form-data"
                           method="post"
                           action="${pageContext.request.contextPath}/admin/insert.htm"
                           modelAttribute="createBlogDto">
                  <div class="form-group">
                    <label for="exampleInputName1">Title</label>
                    <form:input type="text" path ="title"  class="form-control" id="exampleInputName1" placeholder="Blog's title" />
                  </div>
                  <div class="form-group">
                    <label for="exampleInputName1">Subtitle</label>
                    <form:input type="text" path="subtitle" name="subtitle" class="form-control"  placeholder="Blog's subtitle" />
                  </div>
                  <div class="form-group">
                    <label for="categories">Category</label>
                    <div id="categories" class="checkbox-row">
                      <c:forEach var="category" items="${categories}" varStatus="loopStatus">
                        <div class="checkbox-column">
                          <div class="form-check">
                            <label class="form-check-label">
                              <input type="checkbox" class="form-check-input" name="categories" value="${category.slug}">
                                ${category.name}
                              <i class="input-helper"></i>
                            </label>
                          </div>
                        </div>
                      </c:forEach>
                    </div>
                  </div>
<%--                  <div class="form-group">--%>
<%--                    <h6>Thumbnail</h6>--%>
<%--                    <form:input type="file" path="attachment" name="thumbnail"/>--%>
<%--                  </div>--%>
                  <div class="form-group">
                    <label>File upload</label>
                    <form:input type="file" path="attachment" name="attachment" class="file-upload-default" />
                    <div class="input-group col-xs-12">
                      <input type="text" class="form-control file-upload-info"  disabled="true" placeholder="Upload Image" />
                      <span class="input-group-append">
                        <button class="file-upload-browse btn btn-gradient-primary" type="button">Upload</button>
                      </span>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-6">
                      <h6>Popularity</h6>
                      <div class="form-group row">
                        <div class="col-sm-4">
                          <div class="form-check">
                            <label class="form-check-label">
                              <form:radiobutton path="isPopular" value="true" class="form-check-input"/>
                                Make Popular
                              <i class="input-helper"></i>
                            </label>
                          </div>
                        </div>
                        <div class="col-sm-5">
                          <div class="form-check">
                            <label class="form-check-label">
                              <form:radiobutton path="isPopular" value="false" class="form-check-input"/>
                                Common
                              <i class="input-helper"></i>
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="form-group">
                    <div>Content</div>
                    <form:input path="description" id="description" name ="description" style="display: none; !important"/>
                    <div id="editor"></div>
                  </div>
                  <button onclick="saveData()" type="submit" class="btn btn-gradient-primary me-2">Create</button>
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
<script src="${pageContext.request.contextPath}/vendors/js/vendor.bundle.base.js"></script>
<!-- endinject -->
<!-- Plugin js for this page -->
<!-- End plugin js for this page -->
<!-- inject:js -->
<script src="${pageContext.request.contextPath}/js/off-canvas.js"></script>
<script src="${pageContext.request.contextPath}/js/hoverable-collapse.js"></script>
<script src="${pageContext.request.contextPath}/js/misc.js"></script>

<script>
    let editor;
    const value = document.getElementById("description").value
    if (value) {
        const editorData = JSON.parse(document.getElementById("description")?.value)

        editor = new EditorJS({
            holder: "editor",
            onReady: () => {
                console.log("Editor is ready");
            },
            tools: {
                header: {
                    class: Header,
                    config: {
                        placeholder: "Enter a header",
                        levels: [2, 3, 4],
                        defaultLevel: 3,
                    },
                },
                list: {
                    class: List,
                    inlineToolbar: true,
                },
                paragraph: {
                    class: Paragraph,
                    config: {
                        placeholder: "Click here to start typing",
                    },
                    inlineToolbar: true,
                },
                embed: {
                    class: Embed,
                    inlineToolbar: true,
                },
                link: {
                    class: LinkTool,
                    config: {
                        endpoint: "https://your-backend.com/fetchUrl",
                    },
                },
                delimiter: Delimiter,
                code: {
                    class: CodeTool,
                    inlineToolbar: true,
                },
            },
            data: editorData,
            inlineToolbar: true,
            toolbar: {
                buttons: [
                    "header",
                    "bold",
                    "italic",
                    "link",
                    "unorderedList",
                    "orderedList",
                    "embed",
                    "code",
                ],
            },
        });
    }
    else {
        editor = new EditorJS({
            holder: "editor",
            onReady: () => {
                console.log("Editor is ready");
            },
            tools: {
                header: {
                    class: Header,
                    config: {
                        placeholder: "Enter a header",
                        levels: [2, 3, 4],
                        defaultLevel: 3,
                    },
                },
                list: {
                    class: List,
                    inlineToolbar: true,
                },
                paragraph: {
                    class: Paragraph,
                    config: {
                        placeholder: "Click here to start typing",
                    },
                    inlineToolbar: true,
                },
                embed: {
                    class: Embed,
                    inlineToolbar: true,
                },
                link: {
                    class: LinkTool,
                    config: {
                        endpoint: "https://your-backend.com/fetchUrl",
                    },
                },
                delimiter: Delimiter,
                code: {
                    class: CodeTool,
                    inlineToolbar: true,
                },
            },
            inlineToolbar: true,
            toolbar: {
                buttons: [
                    "header",
                    "bold",
                    "italic",
                    "link",
                    "unorderedList",
                    "orderedList",
                    "embed",
                    "code",
                ],
            },
        });
    }

    function saveData() {
        console.log("saved!");
        editor.save().then((data) => {
            const jsonData = JSON.stringify(data);
            document.getElementById("description").value = jsonData;

            document.getElementById("main-container").submit()
        }).catch((error) => {
            console.log("error: ", error);
        });
    }
</script>

<script>
    $(document).ready(function() {
        $('.file-upload-browse').on('click', function() {
            var file = $(this).parent().parent().parent().find('.file-upload-default');
            file.trigger('click');
        });
        $('.file-upload-default').on('change', function() {
            $(this).parent().find('.form-control').val($(this).val().replace(/C:\\fakepath\\/i, ''));
        });
    });
</script>
<!-- endinject -->
<!-- Custom js for this page -->
<!-- End custom js for this page -->
</body>
</html>