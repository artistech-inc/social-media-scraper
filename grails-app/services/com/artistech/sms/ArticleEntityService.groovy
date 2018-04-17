package com.artistech.sms

import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper
import org.apache.commons.lang.exception.ExceptionUtils
import org.grails.core.util.StopWatch
import grails.plugins.rest.client.RestBuilder
import java.util.concurrent.atomic.AtomicInteger

@Transactional
class ArticleEntityService {

    static AtomicInteger base_id = new AtomicInteger(0)
    def executorService

    def loadArticle(Map map) {
        log.warn "loading article entities"
        if (map == null) {
            log.warn "map is null"
        }
        def table = map["tables"]
        String url = map["info"]["url"]
        log.warn "got entities for url ${url}"
        Iterator it = table.keySet().iterator();
        while(it.hasNext()) {
            String type = it.next();
            log.warn "got entity type ${type}"
            for (int k = 0; k < table[type].size(); k++) {
                String name = table[type][k]
                log.warn "got entity name ${name}"
                Integer id = base_id.addAndGet(1);
                String id_str = id.toString()
                ArticleEntity ae = new ArticleEntity(
                    url: url,
                    id_str: id_str,
                    entity_name: name,
                    mention_type: type)
                ae.save(failOnError: true, flush: true)
            }
        }
        return 0
    }

    def loadArticle(String entityTable) {
        log.warn "starting article entity load"
        JsonSlurper slurper = new JsonSlurper();
        log.debug "Entity Table: ${entityTable}"
        def map = null
        try{ 
            log.warn "trying to slurp"
            map = slurper.parseText(entityTable)
            log.warn map.toString()
            log.warn "tried to get map"
        } catch (Exception e) {
            log.error "error parsing Entity Table JSON"
            log.error e.getMessage()
        }
        log.warn "about to load entity table map"
        loadArticle(map)
    }

}
