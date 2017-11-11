package com.artistech.sms

class Link {

    Tweet tweet
    String url
    String contents
    String text

    @Override
    String toString() {
        return url;
    }

    static mapping = {
        contents type: 'text'
        text type: 'text'
    }

    static constraints = {
        tweet nullable: false, unique: ['url']
        url nullable: false
        contents nullable: true
        text nullable: true
    }
}
