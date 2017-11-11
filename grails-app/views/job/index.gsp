<!--
  To change this license header, choose License Headers in Project Properties.
  To change this template file, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <meta name="layout" content="main"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Jobs Exec</title>
</head>
<body>
<h1>Tweet Jobs</h1>
<ul>
    <ul>
        <li><a href="${createLink(action: 'exec')}">Kick off Link Extraction...</a></li>
        <li><a href="${createLink(action: 'downloadLinks')}">Kick off Link Download...</a></li>
        <li><a href="${createLink(action: 'extractText')}">Kick off Text Extraction...</a></li>
    </ul>
</ul>
</body>
</html>
