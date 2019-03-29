<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>

<head>
<title>Activity Recorder</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link
	href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css"
	type="text/css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/styles.css"
	type="text/css" rel="stylesheet">
<link rel="stylesheet"
	href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">
<link
	href="${pageContext.request.contextPath}/resources/css/Footer-with-button-logo.css"
	type="text/css" rel="stylesheet">
</head>

<body id="bodyBack">

	<jsp:include page="fragment/navbar.jsp" />

	<div class="container" id="LogContainer">
		<div class="row vertical-offset-100">
			<div class="col-md-4 col-md-offset-4">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 align="center" class="panel-title">Logowanie</h3>
					</div>
					<div class="panel-body">
						<form accept-charset="UTF-8" role="form" method="post"
							action="login">
							<fieldset>
								<div class="row" id="input">
									<div class="form-group required">
										<div class="col-md-11 ">
											<input class="form-control" name="username"
												placeholder="Nazwa użytkownika" required autofocus
												type="text">
										</div>
									</div>
								</div>
								<div class="row" id="input">
									<div class="form-group required">
										<div class="col-md-11 ">
											<input class="form-control" placeholder="Hasło"
												name="password" type="password" required>
										</div>
									</div>
								</div>
								<div class="form-group">
									<input class="btn btn-lg btn-success btn-block" type="submit"
										value="Zaloguj się">
								</div>

							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="fragment/footer.jsp" />

	<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
	<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
	<script src="resources/js/bootstrap.js"></script>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script
		src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>
</html>