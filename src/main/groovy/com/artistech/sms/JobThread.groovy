package com.artistech.sms

class JobThread implements Runnable{

    @Override
    void run() {
        def originalTweets = Tweet.findAllByRetweeted_status(null)
        Parser p = new Parser()
        println "extracting links..."
        originalTweets.each {
            Tweet tweet = it
            def urls = p.parse(it.contents)
            println it.contents
            urls.each {
                Link link = new Link(tweet:  tweet, url: it).save()
            }
        }
        println "done..."
    }
}
