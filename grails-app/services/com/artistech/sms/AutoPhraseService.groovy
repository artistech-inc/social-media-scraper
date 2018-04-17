package com.artistech.sms

import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper
import org.apache.commons.lang.exception.ExceptionUtils
import org.grails.core.util.StopWatch
import grails.plugins.rest.client.RestBuilder
import java.util.concurrent.atomic.AtomicInteger

@Transactional
class AutoPhraseService {

    static AtomicInteger base_id = new AtomicInteger(0)
    def executorService

    def loadRanks(Map map) {
        log.warn "loading auto phrase ranks"
        if (map == null) {
            log.warn "map is null"
        }
        def ranks = map["ranks"]
        for (int k = 0; k < ranks.size(); k++) {
            String rank = ranks[k][0]
            String name = ranks[k][1]
            log.warn "got auto phrase ${rank} ${name}"
            Integer id = base_id.addAndGet(1)
            String id_str = id.toString()
            AutoPhrase ap = new AutoPhrase(
                id_str: id_str,
                entity_name: name,
                rank: rank)
            ap.save(failOnError: true, flush: true)
        }
        return 0
    }

    def loadRanks(String ranks) {
        log.warn "starting auto phrase load"
        JsonSlurper slurper = new JsonSlurper();
        log.debug "Auto Phrase Ranks: ${ranks}"
        def map = null
        try{ 
            log.warn "trying to slurp"
            map = slurper.parseText(ranks)
            log.warn map.toString()
            log.warn "tried to get map"
        } catch (Exception e) {
            log.error "error parsing Auto Phrase Rank JSON"
            log.error e.getMessage()
        }
        log.warn "about to load auto phrase rank map"
        loadRanks(map)
    }

}
