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
    <link rel="stylesheet" href="vendors/mdi/css/materialdesignicons.min.css">
    <link rel="stylesheet" href="vendors/css/vendor.bundle.base.css">
    <!-- endinject -->
    <!-- Plugin css for this page -->
    <!-- End plugin css for this page -->
    <!-- inject:css -->
    <!-- endinject -->
    <!-- Layout styles -->
    <link rel="stylesheet" href="css/style.css">
    <!-- End layout styles -->
    <link rel="shortcut icon" href="images/favicon.ico" />
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
                            <img src="images/person-man.png" alt="profile">
                            <span class="login-status online"></span>
                            <!--change to offline or busy as needed-->
                        </div>
                        <div class="nav-profile-text d-flex flex-column">
                            <span class="font-weight-bold mb-2"> Blog Platform</span>
                            <span class="text-secondary text-small">Admin</span>
                        </div>
                        <i class="mdi mdi-bookmark-check text-success nav-profile-badge"></i>
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link" href="blogs/index.htm">
                        <span class="menu-title">Icons</span>
                        <i class="mdi mdi-contacts menu-icon"></i>
                    </a>
                </li>

            </ul>
        </nav>
        <!-- partial -->
        <div class="main-panel">
            <div class="content-wrapper">
                <div class="row">
                    <div class="col-12 grid-margin stretch-card">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">Blog</h4>
                                <p class="card-description"> Create New Blog</p>
                                <form:form class="forms-sample" id="main-container" enctype="multipart/form-data"  method="post"  modelAttribute="createBlogDto" action="${pageContext.request.contextPath}/admin/insert.htm">
                                    <div class="form-group">
                                        <label for="exampleInputName1">Title</label>
                                        <form:input type="text" path="title"  class="form-control" id="exampleInputName1" placeholder="Name" />
                                    </div>
                                    <%--                                <div class="form-group">--%>
                                    <%--                                    <label for="exampleSelectGender">Category</label>--%>
                                    <%--                                    <form:select path="slug" class="form-control" id="exampleSelectGender" items="${slugs}">--%>

                                    <%--                                    </form:select>--%>
                                    <%--                                </div>--%>
                                    <div class="form-group">
                                        <label>File upload</label>
                                        <form:input type="file" path="attachment" name="img"  />

                                    </div>
                                    <div class="form-group">
                                        <div>Description</div>
                                        <form:input path="description" id="description" style="display: none; !important"/>
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