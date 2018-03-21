package com.artistech.sms

import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper
import org.apache.commons.lang.exception.ExceptionUtils
import org.grails.core.util.StopWatch
import grails.plugins.rest.client.RestBuilder

@Transactional
class TweetCredService {

    def executorService

    def loadTweet(Map map) {
        String id_str = map["id_str"]
        TweetCred tc = new TweetCred()
        tc.save(failOnError: true, flush: true)
        return tc
    }

    def loadTweet(String cred) {
        JsonSlurper slurper = new JsonSlurper();
        log.debug "Cred: ${cred}"
        try{ 
            def map = slurper.parseText(cred)
            log.warn map.toString()
        } catch (Exception e) {
            log.error "error parsing Cred JSON"
            log.error e.getMessage()
        }
        //loadTweet(map)
    }

}
