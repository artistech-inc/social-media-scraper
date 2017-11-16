package com.artistech.sms

import grails.rest.RestfulController

class RestTweetUserController extends RestfulController {

    static responseFormats = ['json', 'xml']

    RestTweetUserController() {
        super(TweetUser)
    }
}
