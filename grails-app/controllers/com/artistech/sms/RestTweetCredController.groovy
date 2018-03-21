package com.artistech.sms

import grails.rest.RestfulController

class RestTweetCredController  extends RestfulController {

    static responseFormats = ['json', 'xml']

    RestTweetCredController() {
        super(TweetCred)
    }
}
