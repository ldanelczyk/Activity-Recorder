<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="${pageContext.request.contextPath}">Activity
				Recorder</a>
		</div>
		<ul class="nav navbar-nav navbar-right">
			<c:choose>
				<c:when test="${pageContext['request'].userPrincipal != null}">
					<a class="navbar-brand" href="${pageContext.request.contextPath}">${pageContext['request'].userPrincipal.name}</a>
					<li><a href="${pageContext.request.contextPath}/logout">Wyloguj
							się</a></li>
				</c:when>
			</c:choose>
		</ul>
	</div>
</nav>

