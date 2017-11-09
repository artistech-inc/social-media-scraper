package com.artistech.sms

class Scraped {

    Tweet tweet
    String url
    String contents

    static mapping = {
        contents type: 'text'
    }

    static constraints = {
        tweet nullable: false
        url unllable: true
        contents nullable: true
    }
}
