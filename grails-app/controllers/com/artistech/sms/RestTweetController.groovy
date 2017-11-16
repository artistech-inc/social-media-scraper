package com.artistech.sms

import grails.rest.RestfulController

class RestTweetController  extends RestfulController {

    static responseFormats = ['json', 'xml']

    RestTweetController() {
        super(Tweet)
    }
}
