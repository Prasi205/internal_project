<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Success Page</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<style>
.token-input {
	width: 700px;
	height: 100px;
}
</style>
</head>
<body>
	<div class="container mt-5">
		<div class="row">
			<div class="col-md-6">

				<div class="form-group">
					<label for="accessToken">Access Token</label>
					<textarea id="accessToken" class="form-control token-input"
						rows="3" readonly></textarea>
				</div>

				<a href="#" class="btn btn-lg btn-primary btn-block"
					onclick="logout()">Log out</a>
			</div>
		</div>
	</div>
	<script>
		var accessToken = localStorage.getItem("accessToken");
		document.getElementById("accessToken").value = accessToken;
		
		function logout() {
			localStorage.clear();
			window.location.href = "/internal/user/login";
		}
	</script>
</body>
</html>
