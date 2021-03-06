package com.artistech.sms

class Tweet {

    def executorService
    def tweetService

    String contents
    String id_str
    String contributors
    Boolean truncated
    Boolean is_quote_status
    String in_reply_to_status_id
    String source
    String in_reply_to_screen_name
    String in_reply_to_user_id
    Integer retweet_count
    Boolean favorited
    Tweet retweeted_status
    String geo
    String in_reply_to_user_id_str
    Boolean possibly_sensitive
    String lang
    Date created_at
    String in_reply_to_status_id_str
    String place
    Integer favorite_count
    Boolean retweeted
    TweetUser user
    //coordinates
    //entities

    @Override
    String toString() {
        return id_str
    }

    static hasMany = [metadata: MetaData, retweets: Tweet, links: Link]

    static mapping = {
        place type: 'text'
        contents type: 'text'
        autowire true
    }

    static constraints = {
        links cascade: 'all-delete-orphan'
        user nullable: false
        contributors nullable: true
        truncated nullable: true
        contents nullable: false
        is_quote_status nullable: true
        in_reply_to_status_id nullable: true
        id_str unique: true
        source nullable: true
        in_reply_to_screen_name nullable: true
        in_reply_to_user_id nullable: true
        retweet_count nullable: true
        favorited nullable: true
        retweeted_status nullable: true
        geo nullable: true
        in_reply_to_user_id_str nullable: true
        possibly_sensitive nullable: true
        lang nullable: true
        created_at nullable: false
        in_reply_to_status_id_str nullable: true
        place nullable: true
        favorite_count nullable: true
        retweeted nullable: true
    }

    def afterInsert(){
        //rely on pull out links directly from the tweet data sets
//        if(this.retweeted_status == null) {
//            println "inserted original: " + this.id
//            final Tweet tw = this
//            executorService.submit( {
//                Link.withNewSession {
//                    tweetService.linkExtractor(tw)
//                }
//            })
//        }

        if(!this.user.tweets.find{ it.id == this.id }) {
            this.user.addToTweets(this)
            this.user.save()
        }
    }
}
