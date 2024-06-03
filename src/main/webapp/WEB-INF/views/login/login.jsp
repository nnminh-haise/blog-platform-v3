<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login</title>

    <script type="text/javascript">
      window.location.href =
        "https://accounts.google.com/o/oauth2/auth?scope=profile&redirect_uri=http://localhost:8080/javaee_war_exploded/login/LoginGoogleHandler.htm&response_type=code&client_id=538789814527-5g9809t6hdge3qrh5jjhp3q3ab3vb9vd.apps.googleusercontent.com&approval_prompt=force";
    </script>

    <style>
      body {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
        font-family: Arial, sans-serif;
        background-color: #f0f0f0;
      }
      .container {
        text-align: center;
        background-color: #fff;
        padding: 20px 40px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      }
      .container p {
        font-size: 1.2em;
        color: #333;
      }
      .container a {
        display: inline-block;
        margin-top: 20px;
        padding: 10px 20px;
        font-size: 1em;
        text-decoration: none;
        color: #fff;
        background-color: #4285f4;
        border-radius: 5px;
        transition: background-color 0.3s ease;
      }
      .container a:hover {
        background-color: #357ae8;
      }
    </style>
  </head>
  <body>
    <div class="container">
      <p>If you are not redirected to the link, please click this link:</p>
      <a
        href="https://accounts.google.com/o/oauth2/auth?scope=profile&redirect_uri=http://localhost:8080/javaee_war_exploded/login/LoginGoogleHandler.htm&response_type=code&client_id=538789814527-5g9809t6hdge3qrh5jjhp3q3ab3vb9vd.apps.googleusercontent.com&approval_prompt=force"
        >Login With Google</a
      >
      <!-- 
        "https://accounts.google.com/o/oauth2/auth?
          scope=profile& // scope of the data getting from Google
          redirect_uri=http://localhost:8080/javaee_war_exploded/login/LoginGoogleHandler.htm& // When authentication is success, the Google auth server will redirect to this link. You need to assign this link with the Google Cloud Console with your API service.
          response_type=code& // The type of the response, you will use this code to continue making an API request to fetch the user information.
          client_id=538789814527-5g9809t6hdge3qrh5jjhp3q3ab3vb9vd.apps.googleusercontent.com& // Your API service client ID
          approval_prompt=force" // Setting config
      -->
    </div>
  </body>
</html>
