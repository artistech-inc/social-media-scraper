package com.artistech.sms

import grails.gorm.transactions.Transactional

@Transactional
class TweetService {

    def linkExtractor(Tweet tweet) {
        Parser p = new Parser()
        def urls = p.parse(tweet.contents)
        urls.each {
            Link link = new Link(tweet: tweet, url: it)//.save(flush: true)
            println link.save(flush: true)
        }
    }

}
