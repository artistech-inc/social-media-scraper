package com.artistech.sms

import grails.rest.RestfulController

class RestArticleEntityController  extends RestfulController {

    static responseFormats = ['json', 'xml']

    RestArticleEntityController() {
        super(ArticleEntity)
    }
}
