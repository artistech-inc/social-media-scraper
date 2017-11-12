package com.artistech.sms

class Link {

    def linkService

    Tweet tweet
    String url
    String contents
    String resolved

    @Override
    String toString() {
        return url;
    }

    static mapping = {
        contents type: 'text'
        resolved type: 'text'
    }

    static constraints = {
        tweet nullable: false, unique: ['url']
        url nullable: false
        contents nullable: true
        resolved nullable: true
    }

    def afterInsert(){
        runAsync {
            linkService.linkDownloader(this)
            linkService.linkResolver(this)
        }
    }
}
