<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
        uri="http://www.springframework.org/tags/form" prefix="form" %> <%@ page
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>${blog.title}</title>
    <link
            rel="stylesheet"
            href="${pageContext.request.contextPath}/css/main.css"
    />
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
<!--------------------------------------
NAVBAR
--------------------------------------->
<nav class="topnav navbar navbar-expand-lg navbar-light bg-white fixed-top">
    <div class="container">
        <a
                class="navbar-brand"
                href="${pageContext.request.contextPath}/index.htm"
        ><strong>Mundana</strong></a
        >
        <button
                class="navbar-toggler collapsed"
                type="button"
                data-toggle="collapse"
                data-target="#navbarColor02"
                aria-controls="navbarColor02"
                aria-expanded="false"
                aria-label="Toggle navigation"
        >
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="navbar-collapse collapse" id="navbarColor02" style="">
            <ul class="navbar-nav mr-auto d-flex align-items-center">
                <c:forEach var="category" items="${categories}">
                    <li class="nav-item">
                        <a
                                class="nav-link"
                                href="${pageContext.request.contextPath}/blogs/index.htm?category=${category.slug}"
                        >
                                ${category.name}<span class="sr-only">(current)</span>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</nav>
<!-- End Navbar -->

<!--------------------------------------
HEADER
--------------------------------------->
<div class="container">
    <div
            class="jumbotron jumbotron-fluid mb-3 pl-0 pt-0 pb-0 bg-white position-relative"
    >
        <div style="height: 55%;" class="tofront">
            <div class="row justify-content-between">
                <div class="col-md-6 pt-6 pb-6 pr-6 align-self-center">
                    <c:forEach var="category" items="${blogCategoryList}">
                        <p class="text-uppercase font-weight-bold">
                            <a class="text-danger" href="${pageContext.request.contextPath}/blogs/index.htm?category=${category.slug}">
                                    ${category.name}
                            </a>
                        </p>
                    </c:forEach>
                    <h1 class="display-4 secondfont mb-3 font-weight-bold">${blog.title}</h1>
                    <p class="mb-3">${blog.subTitle}</p>
                </div>
                <div class="col-md-6 pr-0">
                    <img
                            style="height: 100%;"
                            src="${blog.thumbnail}"
                    />
                </div>
            </div>
        </div>
    </div>
</div>
<!-- End Header -->

<!--------------------------------------
MAIN
--------------------------------------->
<div class="container pt-4 pb-4" style="margin-top: 150px;">
    <div class="row justify-content-center">
        <div class="col-lg-2 pr-4 mb-4 col-md-12">
            <div class="sticky-top text-center">
                <div class="text-muted">Share this</div>
                <div class="share d-inline-block">
                    <!-- AddToAny BEGIN -->
                    <div class="a2a_kit a2a_kit_size_32 a2a_default_style">
                        <a class="a2a_dd" href="https://www.addtoany.com/share"></a>
                        <a class="a2a_button_facebook"></a>
                        <a class="a2a_button_twitter"></a>
                    </div>
                    <script
                            async
                            src="https://static.addtoany.com/menu/page.js"
                    ></script>
                    <!-- AddToAny END -->
                </div>
            </div>
        </div>
        <div class="col-md-12 col-lg-8">
            <div id="blogDescription" style="display:none; !important;">${blog.description}</div>
            <div id="editor"></div>
        </div>
    </div>
</div>

<%-- READ NEXT Section   --%>
<div class="container pt-4 pb-4">
    <h5 class="font-weight-bold spanborder"><span>Read next</span></h5>
    <div class="row">
        <div class="col-lg-6">
            <div class="card border-0 mb-4 box-shadow h-xl-300">
                <div
                        style="
                                background-image: url('${favouriteBlogs.get(0).thumbnail}');
                                height: 150px;
                                background-size: cover;
                                background-repeat: no-repeat;
                                border-radius: 0.5rem;
                                "
                ></div>
                <div
                        class="card-body px-0 pb-0 d-flex flex-column align-items-start"
                >
                    <h2 class="h4 font-weight-bold">
                        <a class="text-dark" href=""
                        >${favouriteBlogs.get(0).title}</a
                        >
                    </h2>
                    <p class="card-text">
                        ${favouriteBlogs.get(0).subTitle}
                    </p>
                    <div>
                        <small class="d-block"
                        ><a class="text-muted" href="./author.html"
                        >Favid Rick</a
                        ></small
                        >
                        <small class="text-muted">Dec 12 &middot; 5 min read</small>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="flex-md-row mb-4 box-shadow h-xl-300">
                <c:forEach var="blog" items="${favouriteBlogs}" varStatus="status">
                    <c:if test="${status.index > 0}">
                        <div class="mb-3 d-flex align-items-center rounded-lg">
                            <img height="80" src="${blog.thumbnail}" />
                            <div class="pl-3">
                                <h2 class="mb-2 h6 font-weight-bold">
                                    <a class="text-dark" href="${pageContext.request.contextPath}/blogs/${blog.slug}.htm">
                                            ${blog.title}
                                    </a>
                                </h2>
                                <small class="text-muted">${blog.publishAt}</small>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<!-- End Main -->

<!--------------------------------------
FOOTER
--------------------------------------->
<div class="container mt-5">
    <footer class="bg-white border-top p-3 text-muted small">
        <div class="row align-items-center justify-content-between">
            <div>
                <span class="navbar-brand mr-2"><strong>Mundana</strong></span>
                Copyright &copy;
                <script>
                    document.write(new Date().getFullYear());
                </script>
                . All rights reserved.
            </div>
        </div>
    </footer>
</div>
<!-- End Footer -->

<!--------------------------------------
JAVASCRIPTS
--------------------------------------->
<script
        src="./assets/js/vendor/jquery.min.js"
        type="text/javascript"
></script>
<script
        src="./assets/js/vendor/popper.min.js"
        type="text/javascript"
></script>
<script
        src="./assets/js/vendor/bootstrap.min.js"
        type="text/javascript"
></script>
<script src="./assets/js/functions.js" type="text/javascript"></script>
</body>
<script>
    const blogDescriptionData = document.getElementById("blogDescription").innerText;

    // Convert the JSON string to an object
    const editorData = JSON.parse(blogDescriptionData);

    // Initialize EditorJS with the parsed data
    const editor = new EditorJS({
        holder: "editor",
        readOnly: true,
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
        data: editorData,  // Pass the parsed data here
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


    function saveData() {
        console.log("saved!");
        editor.save().then((data) => {
            console.log(data);
            const jsonData = JSON.stringify(data);
            document.getElementById("description").value = jsonData;
            document.getElementById("main-container").submit();
        }).catch((error) => {
            console.log("error: ", error);
        });
    }
</script>
</html>