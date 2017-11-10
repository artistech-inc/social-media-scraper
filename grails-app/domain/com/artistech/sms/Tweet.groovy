package com.artistech.sms

class Tweet {

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
    //use date here?: https://stackoverflow.com/questions/999172/how-to-parse-a-date
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

    static hasMany = [metadata: MetaData, retweets: Tweet]

    static mapping = {
        place type: 'text'
        contents type: 'text'
    }

    static constraints = {
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
        created_at nullable: true
        in_reply_to_status_id_str nullable: true
        place nullable: true
        favorite_count nullable: true
        retweeted nullable: true
    }
}
