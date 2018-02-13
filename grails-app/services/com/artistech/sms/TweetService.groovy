package com.artistech.sms

import grails.gorm.transactions.Transactional

@Transactional
class TweetService {

    /**
    * not being used anymore; instead, links are created in BootStrapService.loadTweet(map)
    **/
    def linkExtractor(Tweet tweet) {
        Parser p = new Parser()
        def urls = p.parse(tweet.contents)
        urls.each {
            Link link = new Link(tweet: tweet, url: it)
            link.save(flush: true)
        }
    }

    /**
    * Find all tweets that are not retweets -- the retweeted_status field
    * is null since it is not pointing to a tweet it is retweeting
    * Returns ArrayList of Tweets
    * 
    **/
    def queryForOriginalTweets() {
        def origTweetsList = Tweet.findAllByRetweeted_status(null)
        log.debug "Number of original tweets: " + origTweetsList.size()
        for (Tweet theTweet : origTweetsList) {
            log.debug "Original tweet:" + theTweet.toString()
            
        }
        return origTweetsList
        
    }
}
