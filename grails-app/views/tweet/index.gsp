<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'tweet.label', default: 'Tweet')}" />
        <g:set var="maxPerPage" value="${10}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-tweet" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <%--
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                --%>
                <li><g:link class="create" action="create">Upload Tweets</g:link></li>
                </ul>
            </div>
            <div id="list-tweet" class="content scaffold-list" role="main">
                <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
                <%--
            <h1>
                
                <div class="buttons">

                    <g:link class="save" params="[sort: 'original', order: 'asc']"
                        controller="tweet" action="index">Show only original Tweets</g:link>
                    </div> 
                </h1>
                --%>
                Total: ${tweetCount}<br>
            <%--
                Parameters: ${params}
            --%>

            <table>
                <tr background:#f7f7f7> 
                    <td width:50% background:#f7f7f7>
                        <g:form controller="Tweet">
                        <p>Refine By </p>
                        <br>
                        <g:checkBox name="original" value="${origTweet}" /> Original Only <br>
                        <g:checkBox name="hasLinks" disabled="true" /> Has Links <br>
                        <g:checkBox name="hasHashtags" disabled="true"/> Has Hashtags <br>
                        <g:checkBox name="hasImages" disabled="true"/> Has Images <br>
                        <g:checkBox name="hasVideo" disabled="true"/> Has Video <br>
                        <br>
                        <p>Language</p>
                        <g:select name="languageSelect"
                            from="${languages}"
                            value="${selectedLang}"
                            noSelection="['':'-Choose-']"
                             />
                        <br> <br>
                        <p>Sort By 
                        </p>
                        
                        <g:checkBox name="sortNumRetweets" value="${sortNumRetweets}"/> # of Retweets (Popularity) <br>
                        <g:checkBox name="sortCredibility" disabled="true" /> Credibility <br>
                        <br>
                        <g:actionSubmit action="index" value="Update View" />
                        </g:form>

                    </td>
                    <td>   
                        <f:table collection="${tweetList}" properties="['id_str', 'created_at', 'contents', 'retweeted_status', 'user']" />
                    </td>
                </tr>
            </table>
            <div class="pagination">
                <g:if test="${tweetCount <= maxPerPage}">                   
                    Showing ${tweetCount} of ${tweetCount}
                </g:if>
                <g:else>
                    <g:paginate total="${tweetCount ?: 0}" />
                </g:else>
            </div>
        </div>
    </body>
</html>