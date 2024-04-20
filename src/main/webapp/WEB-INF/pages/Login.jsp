<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
	integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />
<style>
.error-message {
	color: red;
}
</style>
<title>Login</title>
</head>
<body>
	<div>
		<h1>Welcome</h1>
		<form id="login-form" onsubmit="submitForm(event)">
			<div class="txt_field">
				<label>User name </label> <input type="email"
					placeholder="your-email@gmail.com" name="email" id="email"
					minlength="10" maxlength="320" required oninput="validateEmail()">
				<div id="email-error" class="error-message"></div>
				<br>

			</div>

			<div class="user-box">
				<label>Password </label>&nbsp; <input type="password"
					placeholder="Your Password" name="password" id="password" required
					minlength="8" maxlength="15" oninput="validatePassword()" >
					<div id="password-error" class="error-message"></div><br> <input
					type="checkbox" onclick="myFunction()">Show Password
			</div>
			<br> <input type="submit" value="Login">&nbsp;&nbsp; <input
				type="reset" value="Clear">
		</form>
	</div>
	<script>
            function myFunction() {
                var idValue = document.getElementById("password");
                if (idValue.type === "password") {
                    idValue.type = "text";
                } else {
                    idValue.type = "password";
                }
            }
            
            function validateEmail() {
                var email = document.getElementById("email").value.trim();
                var errorDiv = document.getElementById("email-error");
                var emailPattern = /^[a-zA-Z0-9][\w.%+-]*@[a-zA-Z]+\.[a-zA-Z]{2,}$/;
                if (!emailPattern.test(email)) {
                    errorDiv.innerText = "Invalid email format";
                } else {
                    errorDiv.innerText = "";
                }
            }

            function validatePassword() {
                var password = document.getElementById("password").value.trim();
                var errorDiv = document.getElementById("password-error");
                var passwordPattern = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[#@$!%*?^&])[A-Za-z\d#@$!%*?^&]{8,15}$/;
                if (!passwordPattern.test(password)) {
                    errorDiv.innerText = "\n Password does not match below creteria \n\n Password should be 8 to 15 \n Atleast one Uppercase(A to Z) \n Atleast one Lowercase(a to z) \n Atleast one Digits(0-9) \n Atleast one Special charater(@#$!%*?&) \n Cannot contain space";
                } else {
                    errorDiv.innerText = "";
                }
            }
            
            function submitForm(event) {
                event.preventDefault();
                var email = document.getElementById("email").value;
                var password = document.getElementById("password").value;

                fetch("/internal/user/signin", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email: email, password: password })
                })
                .then(response => response.json())
                .then(data => {
                    if (data.isSuccess) {
                        alert("Login Successful...");
                        localStorage.setItem("accessToken", data.responseData.accessToken);
                        window.location.href = "/internal/user/success";
                    } else {
                         alert(data.message);
                    }
                })
                .catch(error => {
                     alert("Error occurred while logging in."); 
                });
            }
    
        </script>
</body>
</html>
