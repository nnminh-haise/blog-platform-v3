<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
  <title>Error</title>
  <style>
      body {
          font-family: Arial, sans-serif;
          background-color: #f4f4f4;
          margin: 0;
          padding: 0;
          display: flex;
          justify-content: center;
          align-items: center;
          height: 100vh;
      }
      .error-container {
          background: white;
          padding: 20px;
          border-radius: 8px;
          box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
          max-width: 600px;
          text-align: center;
      }
      h1 {
          color: #e74c3c;
          font-size: 24px;
      }
      p {
          font-size: 16px;
          color: #333;
      }
      .back-link {
          display: inline-block;
          margin-top: 20px;
          padding: 10px 20px;
          color: white;
          background-color: #3498db;
          text-decoration: none;
          border-radius: 5px;
          transition: background-color 0.3s ease;
      }
      .back-link:hover {
          background-color: #2980b9;
      }
  </style>
</head>
<body>
<div class="error-container">
  <h1>${errorResponse.error}</h1>
  <p>Message: ${errorResponse.message}</p>
  <p>Description: ${errorResponse.description}</p>
  <a href="${pageContext.request.contextPath}/index.htm" class="back-link">Back to Home</a>
</div>
</body>
</html>
