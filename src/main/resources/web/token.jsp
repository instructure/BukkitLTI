<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html>
<head>
<title>BukkitLTI</title>
<style>
body {
	padding: 0 1em;
}
h1 {
	background: url("/dirt.png") repeat-x;
	padding: 8px;
	height: 24px;
	font-size: 24px;
	padding: 12px;
	color: #fff;
	font-weight: normal;
	border-radius: 5px;
}
ul {
	margin: 0;
	padding: 0;
}
li {
	list-style: none;
	background-color: #ded;
	margin: 1em 0;
	padding: 0.25em 1em;
	border-radius: 5px;
}
</style>
</head>
<body>
<h1>Minecraft</h1>
<form method="POST" action="/assignment">
<ul>
	<li>
		<p>To join, add this address to your multiplayer servers list:</p>
		<pre><c:out value="${address}" /></pre>
	</li>
	<c:if test="${unregistered}">
		<li>
			<p>To register, paste this command into chat:</p>
			<pre>/register <c:out value="${token}" /></pre>
		</li>
	</c:if>
	<c:if test="${studentAssignment}">
		<li>
			<p>To begin the assignment, click <input type="submit" name="effect" value="begin" />
				or paste this command into chat:</p>
			<pre>/assignment begin</pre>
		</li>
		<li>
			<p>To submit the assignment, click <input type="submit" name="effect" value="submit" />
				or paste this command:</p>
			<pre>/assignment submit</pre>
		</li>
	</c:if>
	<c:if test="${teacherAssignment}">
		<li>
			<p>To set the starting position for the assignment, click <input type="submit" name="effect" value="set" />
				or paste this command into chat:</p>
			<pre>/assignment set</pre>
		</li>
		<li>
			<p>To automatically grade a student, enter this command into a command block:</p>
			<pre>/grade @p 0.9</pre>
		</li>
	</c:if>
</ul>
</form>
</body>
</html>