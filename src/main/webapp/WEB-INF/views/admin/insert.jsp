<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en" >
<head>
    <base href="${pageContext.request.contextPath}/" />
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
        }
    </style>
    <!-- endinject -->
    <!-- Plugin css for this page -->
    <!-- End plugin css for this page -->
    <!-- inject:css -->
    <!-- endinject -->
    <!-- Layout styles -->
    <link rel="stylesheet" href="css/style.css">
    <!-- End layout styles -->
    <link rel="shortcut icon" href="images/favicon.ico" />
</head>
<body>
<div class="container-scroller">
    <!-- partial:../../partials/_navbar.html -->
    <nav class="navbar default-layout-navbar col-lg-12 col-12 p-0 fixed-top d-flex flex-row">
        <div class="text-center navbar-brand-wrapper d-flex align-items-center justify-content-center">
            <a class="navbar-brand brand-logo" href="../../index.html"><img src="images/blogg.png" alt="logo" /></a>
            <a class="navbar-brand brand-logo-mini" href="../../index.html"><img src="images/logo-mini.svg" alt="logo" /></a>
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
                    <a class="nav-link" href="blogs/index.htm">
                        <span class="menu-title">Icons</span>
                        <i class="mdi mdi-contacts menu-icon"></i>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="category/index.htm">
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
                        <a href="category/insert.htm" >
                            <button type="button" class="btn btn-gradient-success btn-fw">Add</button>
                        </a>

                    </div>
                </div>
                <div class="row">
                    <div class="col-12 grid-margin stretch-card">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">Blog</h4>
                                <p class="card-description"> Create New Blog</p>
                                <form:form class="forms-sample" enctype="multipart/form-data" method="post" action="admin/insert.htm" modelAttribute="createBlogDto">
                                    <div class="form-group">
                                        <label for="exampleInputName1">Title</label>
                                        <form:input type="text" path ="title"  class="form-control" id="exampleInputName1" placeholder="Name" />
                                    </div>
                                    <div class="form-group">
                                        <label for="exampleInputName1">Subtitle</label>
                                        <form:input type="text" path="subtitle" name="subtitle" class="form-control"  placeholder="Subtitle" />
                                    </div>
                                    <div class="form-group">
                                        <label for="categories">Category</label>
                                        <div id="categories" class="checkbox-row">
                                            <c:forEach var="entry" items="${slugs}" varStatus="loop">
                                            <div class="checkbox-column">
                                                <div class="form-check">
                                                    <label class="form-check-label">
                                                        <input type="checkbox" class="form-check-input" name="cates" value="${entry.key}">
                                                            ${entry.value}
                                                        <i class="input-helper"></i>
                                                    </label>
                                                </div>
                                            </div>
                                            <c:if test="${loop.index % 4 == 3}">
                                        </div>
                                        <div class="checkbox-row">
                                            </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>
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
                                            <div class="form-group row">
                                                <label class="col-sm-3 col-form-label">Popular</label>
                                                <div class="col-sm-4">
                                                    <div class="form-check">
                                                        <label class="form-check-label">
                                                            <form:radiobutton path="is_popular" value="true" class="form-check-input"/> True <i class="input-helper"></i>
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="col-sm-5">
                                                    <div class="form-check">
                                                        <label class="form-check-label">
                                                            <form:radiobutton path="is_popular" value="false" class="form-check-input"/> False <i class="input-helper"></i>
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
                                    <button onclick="saveData()" type="submit" class="btn btn-gradient-primary me-2">Submit</button>
                                    <button type="reset" class="btn btn-light">Cancel</button>
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
<script src="js/file-upload.js"></script>
<script src="js/misc.js"></script>

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