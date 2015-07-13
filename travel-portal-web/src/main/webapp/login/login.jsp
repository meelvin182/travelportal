<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="/css/login.css" media="screen" type="text/css" />
    <link rel="icon" href="/images/icon.png" type="image/x-icon">
    <link rel="shortcut icon" href="/images/icon.png" type="image/x-icon">
    <title>JaaS Login Page</title>
</head>

<body>

<div id="login-form">
    <h1>Authorisation</h1>

    <fieldset>
        <form action="j_security_check" method=post>
            <input type="text" required name="j_username" value="Login" onBlur="if(this.value=='')this.value='Login'" onFocus="if(this.value=='Login')this.value='' ">
            <input type="password" required name="j_password" value="Пароль" onBlur="if(this.value=='')this.value='Пароль'" onFocus="if(this.value=='Пароль')this.value='' ">
            <input type="submit" value="Enter">
            <footer class="clearfix">
                <p><span class="info">?</span><a href="/login/reminder.xhtml">Forgot password?</a></p>
            </footer>
        </form>
    </fieldset>
</div>
</body>
</html>