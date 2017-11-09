package com.artistech.sms

import java.util.concurrent.ExecutorService

class JobThread implements Runnable{

    ExecutorService service

    @Override
    void run() {
        def ids = Tweet.executeQuery( "select id from Tweet" )
        Parser p = new Parser()
        ids.each {
            Tweet tweet = Tweet.get(it)
            print tweet.id + ":  "
            println tweet.contents

            def urls = p.parse(tweet.contents)
            urls.each {
                println it
            }
//            JobProc job = new JobProc(it)
//            service.submit(job)
        }
    }
}
