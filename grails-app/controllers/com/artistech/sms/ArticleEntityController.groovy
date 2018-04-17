package com.artistech.sms

class ArticleEntityController {

    def articleEntityService
    def executorService

    //static scaffold = Cred

    def upload() {
        log.warn "made it to article entity upload!" 
        log.warn request.getMethod()
        //request.getHeaderNames().each { h ->
        //    log.warn h + " : "
        //    request.getHeaders(h).each { n ->
        //        log.warn "    " + n
        //    }
        //}
        if(request.getMethod().toLowerCase() == "post") {
            //this is a REST call to insert a JSON string
            String aeJSON = request.reader.text
            log.warn aeJSON
            articleEntityService.loadArticle(aeJSON)
            render "OK"
            return
        } else {
            render "Expecting POST call\n"
            return
        }
    }
    
    def index() {}
}
