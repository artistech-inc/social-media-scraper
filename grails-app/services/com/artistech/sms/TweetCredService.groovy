package com.artistech.sms

import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper
import org.apache.commons.lang.exception.ExceptionUtils
import org.grails.core.util.StopWatch
import grails.plugins.rest.client.RestBuilder
import java.util.concurrent.atomic.AtomicInteger

@Transactional
class TweetCredService {

    static AtomicInteger base_id = new AtomicInteger(0)
    def executorService

    def loadTweet(Map map) {
        log.warn "loading tweet map"
        if (map == null) {
            log.warn "map is null"
        }
        //String id_str = map["id_str"]
        //log.warn "got id_str ${id_str}"
        Integer id = base_id.addAndGet(1)
        String id_str = id.toString()
        def m1 = map["urls"]
        String url = m1["url"]
        String content = m1["text"]
        String reliability_score = Float.parseFloat((m1[0]["classifiers"][0]["result"][1][1]).toString())
        String bias_score = Float.parseFloat((m1[0]["classifiers"][1]["result"][1][1]).toString())
        String subjectivity_title_score = Float.parseFloat((m1[0]["classifiers"][2]["result"][0]).toString())
        String subjectivity_body_score = Float.parseFloat((m1[0]["classifiers"][2]["result"][1]).toString())
        log.warn "got url ${url}"
        log.warn "got rel score ${reliability_score}"
        log.warn "got bias score ${bias_score}"
        log.warn "got subj title ${subjectivity_title_score}"
        log.warn "got subj body ${subjectivity_body_score}"
        TweetCred tc = new TweetCred(
            url: url,
            id_str: id_str,
            contents: content,
            reliable_style: reliability_score,
            biased_style: bias_score,
            subjectivity_title: subjectivity_title_score,
            subjectivity_body: subjectivity_body_score)
        tc.save(failOnError: true, flush: true)
        return tc
    }

    def loadTweet(String cred) {
        log.warn "starting cred"
        JsonSlurper slurper = new JsonSlurper();
        log.debug "Cred: ${cred}"
        def map = null
        try{ 
            log.warn "trying to slurp"
            map = slurper.parseText(cred)
            log.warn map.toString()
            log.warn "tried to get map"
        } catch (Exception e) {
            log.error "error parsing Cred JSON"
            log.error e.getMessage()
        }
        log.warn "about to load tweet map"
        loadTweet(map)
    }

}
