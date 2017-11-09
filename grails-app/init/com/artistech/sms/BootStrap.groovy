package com.artistech.sms

import groovy.json.JsonSlurper

class BootStrap {

    def bootStrapService

    def init = { servletContext ->

        def tweetsFile = new File('/work/tweets.json')
        println "loading..."
        if(tweetsFile.exists() && Tweet.count() == 0) {
            bootStrapService.loadFile(tweetsFile)
        }
        println "loaded..."
    }
    def destroy = {
    }
}
