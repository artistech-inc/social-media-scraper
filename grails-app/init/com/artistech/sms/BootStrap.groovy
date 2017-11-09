package com.artistech.sms

class BootStrap {

    def bootStrapService

    def init = { servletContext ->

        def tweetsFile = new File('/work/tweets.json')
        println "loading..."
        if(tweetsFile.exists() && Tweet.count() == 0) {
            bootStrapService.loadFile(tweetsFile)
        }
        int counter = 1
        def ret = [];
        println "loaded..."
    }
    def destroy = {
    }
}
