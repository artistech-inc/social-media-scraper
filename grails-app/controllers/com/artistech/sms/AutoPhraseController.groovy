package com.artistech.sms

class AutoPhraseController {

    def autoPhraseService
    def executorService

    //static scaffold = Cred

    def upload() {
        log.warn "made it to auto phrase upload!" 
        log.warn request.getMethod()
        //request.getHeaderNames().each { h ->
        //    log.warn h + " : "
        //    request.getHeaders(h).each { n ->
        //        log.warn "    " + n
        //    }
        //}
        if(request.getMethod().toLowerCase() == "post") {
            //this is a REST call to insert a JSON string
            String apJSON = request.reader.text
            log.warn apJSON
            autoPhraseService.loadRanks(apJSON)
            render "OK"
            return
        } else {
            render "Expecting POST call\n"
            return
        }
    }
    
    def index() {}
}
