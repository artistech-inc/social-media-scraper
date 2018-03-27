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
    * @params String sortOrder -- DESC or ASC order or "" if no sortBy is given
    *           String sortBy -- POPULARITY or CREDIBILITY or ""
    *               popularity is the number of tweets that were retweets of this one
    *           int offset -- where to start a subset of the results
    *           int max -- how many results to return
    * Returns ArrayList of Tweets ordered by number of retweets of this
    */
    def queryForOriginalTweets(String sortOrder, String sortBy, long offsetNum, int maxNum, 
                                String selectedLang) {
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
        // add the selected language part of the query
        if (selectedLang != null && !selectedLang.equals("") ) {
            log.debug "query with selected language: ${selectedLang}"
            query = query + " and lang = '${selectedLang}' \n\
                            "
        }
        // add the Order by part, either popularity or credibility
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
        
        log.debug "queryForOriginalTweets, Number of original tweets: " + origTweetsList.size()
        
        for (Tweet theTweet : origTweetsList) {
            log.debug "Original tweet:" + theTweet.toString()
            
        }
         
        return origTweetsList
    }
    
    /**
     * Filter the tweets based on the parameters given
     * @params 
     *         String sortOrder -- DESC or ASC order or "" if no sortBy is given
     *         String sortBy -- POPULARITY or CREDIBILITY or DATE
     *               popularity is the number of tweets that were retweets of this one;
     *               default will be Date if user doesn't specify; 
     *               need to order by some field so the paging works correctly
     *         int offset -- where to start a subset of the results
     *         int max -- how many results to return
     *         String selectedLang -- language of the contents, or null or ""
     *         String originalTweet -- "on" or null; not a retweet of another tweet
     *         String hasHash -- has hash tags in content (future impl)
     *         String hasLinks -- future impl
     *         String hasVideo -- future impl
     * @returns [tweetList: (list of tweets), tweetCount: (total # that satisfy the query,
     *                                                      so paging will work)]
     **/
    
    def queryForTweets(String sortOrder, String sortBy, long offsetNum, int maxNum,
                        String selectedLang, String originalTweet ) {
        def query = "\n\
                Select tweet From Tweet tweet \n\
                "
        // will need to perform 2 queries, in order to get the count;
        // see if this can be done a different way
        def queryCount = "\n\
                Select count(*) From Tweet tweet \n\
                "
        log.debug "queryForTweets: sortBy: ${sortBy}, sortOrder: ${sortOrder}, lang: ${selectedLang}, original: ${originalTweet}"
        
        // add the Where if either query type is chosen
        if ((selectedLang != null && !selectedLang.equals("")) || originalTweet != null) {
            log.debug "-- found lang or orig tweet option"
            query = query + "Where \n\
                            "
            queryCount = queryCount + "Where \n\
                            "
        }
        // add language clause
        if (selectedLang != null && !selectedLang.equals("")) {
            query = query + "lang = '${selectedLang}' \n\
                            "
            queryCount = queryCount + "lang = '${selectedLang}' \n\
                            "
        }
        // if language was selected, then need an "and" if original tweet also selected
        if ((selectedLang != null && !selectedLang.equals("")) && originalTweet != null) {
            query = query + "and \n\
                            "
            queryCount = queryCount + "and \n\
                            "
        }
        // if original tweet selected, now add in the expression for that;
        // may or may not have an 'and' in front of it if language was selected before it
        if (originalTweet != null) {
            query = query + "tweet.retweeted_status = null \n\
                            "
            queryCount = queryCount + "tweet.retweeted_status = null \n\
                            "
        }
         
        // add the Order By clause
        // don't need to add it to the query that's for the count only
        switch (sortBy) {
            case "POPULARITY": query = query + "Order by size(tweet.retweets) \n\
                        ${sortOrder}"; break;
            case "DATE": query = query + "Order by created_at\n\
                        ${sortOrder}"; break;
            //case "CREDIBILITY"
            default: log.debug "No sortBy criteria"
        }       

        log.debug "query: ${query}, queryCount: ${queryCount}"
        
        def ArrayList<Tweet> tweetsList = Tweet.executeQuery(query, [offset:offsetNum, max:maxNum])
        //query again, this time only for the count
        def countResult = Tweet.executeQuery(queryCount)
        def tweetCount = countResult[0]
        
        log.debug "queryForTweets, Number of tweets: " + tweetCount
        
        for (Tweet theTweet : tweetsList) {
            log.debug " tweet: ${theTweet.toString()}, id: ${theTweet.id}"
            
        }        
        
        return [tweetList: tweetsList, tweetCount: tweetCount]
        
    }
    
    def queryTestReturnCount() {
        String query = "\n\
                Select tweet, count(*) From Tweet tweet \n\
                Where tweet.retweeted_status = null \n\
                Order by size(tweet.retweets) \n\
                DESC"
        log.debug "queryTestReturnCount, query: ${query}"
        
        def results = Tweet.executeQuery(query, [offset:0, max:10])
        // results should be a list of a list of tweets and a count
        def tweetList = results[0]
        def tweetCount = results[1]
        log.debug "    tweetCount: ${tweetCount}"
        // hopefully tweetList is an array of tweets
        tweetList.each {
            log.debug "tweet ${it}"
        }
    }
    
    /**
     * 
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
      /*
      for (String lang : results) {
            log.debug "Languages:" + lang
            
        }   
      */
            
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
