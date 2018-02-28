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
    
    /**
    * Find all tweets that are not retweets -- the retweeted_status field
    * is null since it is not pointing to a tweet it is retweeting.
    * Order by the number of retweets this has.
    * 
    * @params String sortOrder -- DESC or ASC order
    *           String sortBy -- POPULARITY or CREDIBILITY
    *               popularity is the number of tweets that were retweets of this one
    *           int offset -- where to start a subset of the results
    *           int max -- how many results to return
    * Returns ArrayList of Tweets ordered by number of retweets of this
    */
    def queryForOriginalTweets(String sortOrder, String sortBy, long offsetNum, int maxNum) {
        /*
        String query = "\n\
                        Select tweet From Tweet tweet \n\
                        Where tweet.retweeted_status = null \n\
                        Order by size(tweet.retweets) \n\
                        ${sortOrder}" 
                        */
        String query = "\n\
                        Select tweet From Tweet tweet \n\
                        Where tweet.retweeted_status = null \n\
                        "
        switch (sortBy) {
            case "POPULARITY": query = query + "Order by size(tweet.retweets) \n\
                        ${sortOrder}"; break;
            //case "CREDIBILITY"
            default: log.debug "No sortBy criteria"
        }
        
        // [offset:0, max:5]
        log.debug "queryForOriginalTweets, offset and max: " + offsetNum + ", " + maxNum
        log.debug "queryForOriginalTweets, query: " + query               
        def ArrayList<Tweet> origTweetsList = Tweet.executeQuery(query, [offset:offsetNum, max:maxNum])
        
        log.debug "queryForOriginalTweets, Number of original tweets, ordered by number of retweets: " + origTweetsList.size()
        for (Tweet theTweet : origTweetsList) {
            log.debug "Original tweet:" + theTweet.toString()
            
        }
         
        return origTweetsList
    }
    
    /**
    * Find unique set of languages used in the tweet content.
    * Field in the Tweet is lang.
    * Database values are returned, as in en, ir, ar...
    * Returns list of languages
    *
    */
    
   def queryForUniqueLanguages() {
      def results = Tweet.withCriteria {
          projections {
              distinct("lang")
            }
          }
      for (String lang : results) {
            log.debug "Languages:" + lang
            
        }         
            
      return results
   }
   
   
   /**
   * Search the content of the tweet, ignoring case
   * @params String searchString
   * 
   * Returns list of Tweets containing the string
   **/
   def queryTweetContent(String searchString) {
       def results = Tweet.findAllByContentsIlike("%${searchString}%")  
       /*
       for (Tweet theTweet : results) {
            log.debug "tweet content:" + theTweet.contents           
        }  
        */
       log.debug "queryTweetContent, number of results: " + results.size() 
       return results
       
   }
}
