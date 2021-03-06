package com.artistech.sms

class Link {

    def executorService
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
        autowire true
    }

    static constraints = {
        tweet nullable: false, unique: ['url']
        url nullable: false
        contents nullable: true
        resolved nullable: true
    }

    def beforeInsert() {
    }

    def afterInsert() {
        executorService.submit( {
            Link.withNewSession {
                linkService.linkData(this).save()
            }
        })
        if(!this.tweet.links.find{ it.id == this.id }) {
            this.tweet.addToLinks(this)
            this.tweet.save()
        }
    }
}
