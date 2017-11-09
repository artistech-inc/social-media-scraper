package com.artistech.sms

class JobProc implements Runnable {

    Tweet tweet

    JobProc(Tweet tweet) {
        this.tweet = tweet
    }

    @Override
    void run() {
        def urls = p.parse(tweet.text)
        urls.each {
            println it
        }
    }
}
