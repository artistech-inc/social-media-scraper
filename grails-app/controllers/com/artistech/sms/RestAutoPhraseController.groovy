package com.artistech.sms

import grails.rest.RestfulController

class RestAutoPhraseController  extends RestfulController {

    static responseFormats = ['json', 'xml']

    RestAutoPhraseController() {
        super(AutoPhrase)
    }
}
