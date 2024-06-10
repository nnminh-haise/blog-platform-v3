<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en" >
<head>
  <!-- Required meta tags -->
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Blog Admin</title>
  <!-- plugins:css -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/vendors/mdi/css/materialdesignicons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/vendors/css/vendor.bundle.base.css">
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
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
      }  .btn-gradient-green {
           color: white !important;
           background: linear-gradient(to right, #84d9d2, #07cdae) !important;
         }
  </style>

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
        <img src="images/blogg.png" alt="logo" />
      </a>
      <a class="navbar-brand brand-logo-mini" href="${pageContext.request.contextPath}/index.htm">
        <img src="images/logo-mini.svg" alt="logo" />
      </a>
    </div>
  </nav>
  <!-- partial -->
  <div class="container-fluid page-body-wrapper">
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
        <li class="nav-item btn-gradient-green">
          <a class="nav-link" href="${pageContext.request.contextPath}/admin/index.htm">
            <span style="color: white" class="menu-title">Blogs</span>
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
          <h3>${selectingBlog.title}</h3>
        </div>
        <div class="row">
          <div class="col-12 grid-margin stretch-card">
            <div class="card">
              <div class="card-body">
                <form:form class="forms-sample"
                           enctype="multipart/form-data"
                           method="post"
                           action="${pageContext.request.contextPath}/admin/edit/${selectingBlog.slug}.htm"
                           modelAttribute="updateBlogDto">
                  <div class="form-group">
                    <label for="exampleInputName1">Title</label>
                    <form:input type="text" path ="title"  class="form-control" id="exampleInputName1" placeholder="Blog's title" />
                  </div>
                  <div class="form-group">
                    <label for="exampleInputName1">Subtitle</label>
                    <form:input path="subTitle" type="text" name="subtitle" class="form-control"   placeholder="Blog's Subtitle" />
                  </div>
                  <div class="form-group">
                    <label for="categories">Category</label>
                    <div id="categories" class="checkbox-row">
                      <c:forEach var="category" items="${blogCategories}" varStatus="loopStatus">
                        <div class="checkbox-column">
                          <div class="form-check">
                            <label class="form-check-label">
                              <input type="checkbox" class="form-check-input" name="categories" value="${category.slug}" checked>
                                ${category.name}
                              <i class="input-helper"></i>
                            </label>
                          </div>
                        </div>
                      </c:forEach>
                      <c:forEach var="category" items="${otherCategories}" varStatus="loopStatus">
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
                  <div class="form-group">
                    <h6>Thumbnail</h6>
                    <form:input type="file" path="attachment" name="thumbnail"/>
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
                    <div>Description</div>
                    <form:input path="description" id="description" name ="description" style="display: none; !important"/>
                    <div id="editor"></div>
                  </div>
                  <button onclick="saveData()" type="submit" class="btn btn-gradient-primary me-2">Save</button>
                </form:form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- main-panel ends -->
  </div>
  <!-- page-body-wrapper ends -->
</div>
<!-- container-scroller -->
<style>
    .checkbox-row {
        display: flex;
        flex-wrap: wrap;
    }
    .checkbox-column {
        flex: 0 0 25%; /* 25% width for 4 columns per row */
    }
</style>
<!-- plugins:js -->
<script src="vendors/js/vendor.bundle.base.js"></script>
<!-- endinject -->
<!-- Plugin js for this page -->
<!-- End plugin js for this page -->
<!-- inject:js -->
<script src="js/off-canvas.js"></script>
<script src="js/hoverable-collapse.js"></script>
<script src="js/misc.js"></script>
<script src="js/file-upload.js"></script>
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
                image: {
                    class: ImageTool,
                    config: {
                        endpoints: {
                            byFile: "https://your-backend.com/uploadFile",
                            byUrl: "https://your-backend.com/fetchUrl",
                        },
                    },
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
                    "image",
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
                image: {
                    class: ImageTool,
                    config: {
                        endpoints: {
                            byFile: "https://your-backend.com/uploadFile",
                            byUrl: "https://your-backend.com/fetchUrl",
                        },
                    },
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
                    "image",
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
<!-- endinject -->
<!-- Custom js for this page -->
<!-- End custom js for this page -->
</body>
</html>