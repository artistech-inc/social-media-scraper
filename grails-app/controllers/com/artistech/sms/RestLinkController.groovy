package com.artistech.sms

import grails.rest.RestfulController

class RestLinkController extends RestfulController {

    static responseFormats = ['json', 'xml']

    static scaffold = Link

    RestLinkController() {
        super(Link)
    }
}
