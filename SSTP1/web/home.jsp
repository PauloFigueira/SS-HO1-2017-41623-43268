<!DOCTYPE html>
<html >
<head>
  <meta charset="UTF-8">
  <title>Login/Logout animation concept</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes">
  
  <link rel='stylesheet prefetch' href='https://fonts.googleapis.com/css?family=Open+Sans'>

      <link rel="stylesheet" href="Style/css/style.css">

  
</head>

<body>

  <div class="cont">
  <div class="demo">
    <div class="login">
     <div class="welcome__form"> 
      <form name=“welcomeform”
      action="${pageContext.request.contextPath}/home" method="POST">
        <input type="submit" name="action"  class="login__submit" value="Create User">
        <input type="submit" name="action"  class="login__submit" value="Delete User">
        <input type="submit" name="action"  class="login__submit" value="Change Password">
        <input type="submit" name="action" class="login__submit" value="Logout">
        </form>
      </div>
    </div>
       </div>
  </div>

</body>
</html>
