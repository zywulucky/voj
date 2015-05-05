<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${language}" />
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@propertyConfigurer.getProperty('cdn.url')" var="cdnUrl" />
<!DOCTYPE html>
<html lang="${language}">
<head>
    <meta charset="UTF-8">
    <title>Submission | Verwandlung Online Judge</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="谢浩哲">
    <!-- Icon -->
    <link href="${cdnUrl}/img/favicon.ico" rel="shortcut icon">
    <!-- StyleSheets -->
    <link rel="stylesheet" type="text/css" href="${cdnUrl}/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="${cdnUrl}/css/bootstrap-responsive.min.css" />
    <link rel="stylesheet" type="text/css" href="${cdnUrl}/css/flat-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="${cdnUrl}/css/font-awesome.min.css" />
    <link rel="stylesheet" type="text/css" href="${cdnUrl}/css/style.css" />
    <link rel="stylesheet" type="text/css" href="${cdnUrl}/css/submissions/submissions.css" />
    <!-- JavaScript -->
    <script type="text/javascript" src="${cdnUrl}/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="${cdnUrl}/js/bootstrap.min.js"></script>
    <!--[if lte IE 9]>
        <script type="text/javascript" src="${cdnUrl}/js/jquery.placeholder.min.js"></script>
    <![endif]-->
    <!--[if lte IE 7]>
        <link rel="stylesheet" type="text/css" href="${cdnUrl}/css/font-awesome-ie7.min.css" />
    <![endif]-->
    <!--[if lte IE 6]>
        <script type="text/javascript"> 
            window.location.href='../not-supported';
        </script>
    <![endif]-->
</head>
<body>
    <!-- Header -->
    <%@ include file="/WEB-INF/views/include/header.jsp" %>
    <!-- Content -->
    <div id="content" class="container">
        <div id="main-content" class="row-fluid">
            <div id="submission" class="span12">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th class="flag">Result</th>
                            <th class="score">Score</th>
                            <th class="time">Time</th>
                            <th class="memory">Memory</th>
                            <th class="name">Name</th>
                            <th class="user">User</th>
                            <th class="language">Language</th>
                            <th class="submit-time">Submit Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="submission" items="${submissions}">
                        <tr>
                            <td class="flag-${submission.judgeResult.judgeResultSlug}"><a href="<c:url value="/submission/${submission.submissionId}" />">${submission.judgeResult.judgeResultName}</a></td>
                            <td class="score">${submission.judgeScore}</td>
                            <td class="time">${submission.usedTime} ms</td>
                            <td class="memory">${submission.usedMemory} K</td>
                            <td class="name"><a href="<c:url value="/p/${submission.problem.problemId}" />">P${submission.problem.problemId} ${submission.problem.problemName}</a></td>
                            <td class="user"><a href="<c:url value="/accounts/user/${submission.user.uid}" />">${submission.user.username}</a></td>
                            <td class="language">${submission.language.languageName}</td>
                            <td class="submit-time">
                            	<fmt:formatDate value="${submission.submitTime}" type="both" dateStyle="default" timeStyle="default"/>
                            </td>
                        </tr>
                        </c:forEach>
                        <tr class="more-submissions">
                            <td colspan="8">More Submission...</td>
                        </tr>
                    </tbody>
                </table>
            </div> <!-- #problems -->
        </div> <!-- #main-content -->
    </div> <!-- #content -->
    <!-- Footer -->
    <%@ include file="/WEB-INF/views/include/footer.jsp" %>
    <!-- Java Script -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script type="text/javascript" src="${cdnUrl}/js/site.js"></script>
</body>
</html>