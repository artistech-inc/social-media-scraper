package com.artistech.sms

import grails.gorm.transactions.Transactional

@Transactional
class TweetService {

    def linkExtractor(Tweet tweet) {
        Parser p = new Parser()
        println "extracting links..."
        if(tweet.retweeted_status == null) {
            def urls = p.parse(tweet.contents)
            urls.each {
                Link link = new Link(tweet: tweet, url: it).save()
            }
        }
    }

}
