package com.artistech.sms

class TweetCredController {

    def tweetCredService
    def executorService

    //static scaffold = Cred

    def upload() {
        log.warn "made it to tweet cred upload!" 
        log.warn request.getMethod()
        //request.getHeaderNames().each { h ->
        //    log.warn h + " : "
        //    request.getHeaders(h).each { n ->
        //        log.warn "    " + n
        //    }
        //}
        if(request.getMethod().toLowerCase() == "post") {
            //this is a REST call to insert a JSON string
            String credJSON = request.reader.text
            //log.warn credJSON
            tweetCredService.loadTweet(credJSON)
            render "OK"
            return
        } else {
            render "Expecting POST call\n"
            return
        }
    }
    
    def index() {}
}
