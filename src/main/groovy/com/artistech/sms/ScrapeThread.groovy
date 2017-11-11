package com.artistech.sms

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import java.util.concurrent.ExecutorService

class ScrapeThread implements Runnable {
    ExecutorService service

    @Override
    void run() {
        println "scraping HTML..."
        def ids = Link.executeQuery( "select id from Link" );
        ids.each {
            Link link = Link.get(it)
            try {
                if(link.contents != null && link.text == null) {
                    println link.url
                    Document doc = Jsoup.parse(link.contents)
                    String text = doc.body().text()
                    link.text = text
                    link.save(flush: true)
                }
            } catch (Exception ex) {
                println ex
            }
        }
        println "done..."
    }

}