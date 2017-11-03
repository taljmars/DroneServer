<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Drone DB Status - ${title}</title>
</head>
	<body>
	<h1>Database Picture</h1>
	<c:forEach items="${status}" var="entry">
	    <br><h2> ${entry.key} (${fn:length(entry.value) - 1})</h2></p>
		<table border=1>
			<thead>
				<tr>
					<c:forEach var="s" items="${entry.value[0]}">
						<td align="center">
							<div style="font-weight: bold">${s}</div>
						</td>
					</c:forEach>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="subList" items="${entry.value}" begin="1">
					<tr>
						<c:forEach var="s" items="${subList}">
							<td align="center">
								<c:out value="${s}"/>
							</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>
    </c:forEach>
	<br>
	<br>
	</body>
</html>