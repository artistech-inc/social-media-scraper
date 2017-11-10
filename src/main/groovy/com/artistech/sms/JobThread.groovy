package com.artistech.sms

import java.util.concurrent.ExecutorService

class JobThread implements Runnable{

    ExecutorService service

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
