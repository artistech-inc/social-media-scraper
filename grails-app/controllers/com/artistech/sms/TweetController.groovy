package com.artistech.sms

class TweetController {

    def bootStrapService
    def executorService

    static scaffold = Tweet

    def upload(TweetCommand cmd) {
        if (cmd == null) {
            redirect view: 'index'
            return
        }

        if (cmd.hasErrors()) {
            println cmd.errors
            respond(cmd.errors, view: 'create')
            return
        }

        executorService.submit {
            bootStrapService.loadFile(cmd)
        }

        redirect action: "index"
    }

    def index() {
        if(request.post) {
            //this is a REST call to insert a JSON string
            String tweet = request.reader.text
            bootStrapService.loadTweet(tweet)
            render "OK"
            return
        }
        long offset = params.offset == null ? 0 : params.offset as long
        int max = params.max == null ? 10 : params.max as int
        [tweetList: Tweet.list(offset: offset, max: max), tweetCount: Tweet.count()]
    }
}
