<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><!DOCTYPE html>
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

	<div class="row" id="BgImages">
		<div class="col-md-2 col-md-offset-1">
			<div class="container">
				<img src="resources/css/images/sil.jpg" class="img-thumbnail"
					alt="sil">
			</div>
		</div>

		<div class="col-md-2 col-md-offset-6">
			<div class="container">
				<img src="resources/css/images/tst.jpg" class="img-thumbnail"
					alt="tst">
			</div>
		</div>
	</div>

	<div class="container" id="RegContainer">
		<div class="row vertical-offset-200">
			<div class="col-md-4 col-md-offset-4">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 align="center" class="panel-title">
							<b>Rejestracja czynności</b>
						</h3>
					</div>
					<div class="panel-body">
						<form class="form-signin" method="post"
							action="${pageContext.request.contextPath}/startRegistration">
							<fieldset>
								<div class="row" id="input">
									<div class="form-group required">
										<div class="col-md-4">
											Nr linii: <select class="form-control" name="machineNumber">
												<option value="AOI1">AOI1</option>
												<option value="AOI2">AOI2</option>
											</select>
										</div>
										<div class="col-md-4" id="WoTextField">
											Numer ZR: <input class="form-control" name="workOrder"
												required autofocus type="text">
										</div>
									</div>
								</div>
								<div class="row" id="input">
									<div class="form-group required">
										<div class="col-md-4">
											Strona: <select class="form-control" name="side">
												<option value="TOP">TOP</option>
												<option value="BOT">BOT</option>
											</select>
										</div>
										<div class="col-md-8">
											Rodzaj czynności: <select class="form-control"
												name="activityType">
												<option value="pisanie programu AOI">pisanie
													programu AOI</option>
												<option value="poprawa programu AOI">poprawa
													programu AOI</option>
											</select>
										</div>
									</div>
								</div>
								<div class="row" id="input2">
									<div class="form-group required">
										<div class="col-md-12">
											Uwagi:
											<textarea class="form-control" name="comments" rows="5"
												id="comment"></textarea>
										</div>
									</div>
								</div>
								<div class="form-group">
									<input class="btn btn-lg btn-success btn-block" type="submit"
										value="Start">
								</div>
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="fragment/footer.jsp" />

	<script src="resources/js/stopwatch.js"></script>
	<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
	<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
	<script src="resources/js/bootstrap.js"></script>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script
		src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>