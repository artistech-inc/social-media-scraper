<!--
  To change this license header, choose License Headers in Project Properties.
  To change this template file, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'tweet.label', default: 'Tweet')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-tweet" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <g:uploadForm name="upload" action="upload" style="display:inline;">
            <fieldset class="form">
                <table border="0">
                    <tr>
                        <td width="150px" align="right"><label for="emailAddress">E-Mail Address: </label></td><td><span class='required-indicator' style='float:left'>*</span><input id="emailAddress" type="email" name="emailAddress" /></td>
                    </tr>
                    <tr>
                        <td width="150px" align="right"><label for="tweetJsonFile">Tweet Dataset: </label></td><td><span class='required-indicator' style='float:left'>*</span><input id="tweetJsonFile" type="file" name="tweetJsonFile"/></td>
                    </tr>
                </table>
            </fieldset>
            <fieldset class="buttons">
                <input class="save" type="submit" value="Upload"/>
            </fieldset>
        </g:uploadForm>
    </body>
</html>
