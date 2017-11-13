<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'link.label', default: 'Link')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#show-link" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="show-link" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <ol class="property-list link">

                <li class="fieldcontain">
                    <span id="tweet-label" class="property-label">Tweet</span>
                    <div class="property-value" aria-labelledby="tweet-label"><a href="${createLink(controller: "tweet", action: "show", id: tweetdbid)}">${tweetid}</a></div>
                </li>

                <li class="fieldcontain">
                    <span id="url-label" class="property-label">Url</span>
                    <div class="property-value" aria-labelledby="url-label"><a href="${url}">${url}</a></div>
                </li>

                <li class="fieldcontain">
                    <span id="resolved-label" class="property-label">Resolved</span>
                    <div class="property-value" aria-labelledby="resolved-label"><a href="${resolved}">${resolved}</a></div>
                </li>

                <li class="fieldcontain">
                    <span id="contents-label" class="property-label">Contents</span>
                    <div class="property-value" aria-labelledby="contents-label">${contents}</div>
                </li>

            </ol>
                        %{--<f:display bean="link" />--}%
        </div>
    </body>
</html>
