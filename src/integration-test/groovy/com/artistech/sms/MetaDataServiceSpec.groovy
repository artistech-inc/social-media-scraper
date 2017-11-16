package com.artistech.sms

import grails.test.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class MetaDataServiceSpec extends Specification {

    MetaDataService metaDataService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new MetaData(...).save(flush: true, failOnError: true)
        //new MetaData(...).save(flush: true, failOnError: true)
        //MetaData metaData = new MetaData(...).save(flush: true, failOnError: true)
        //new MetaData(...).save(flush: true, failOnError: true)
        //new MetaData(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //metaData.id
    }

    void "test get"() {
        setupData()

        expect:
        metaDataService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<MetaData> metaDataList = metaDataService.list(max: 2, offset: 2)

        then:
        metaDataList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        metaDataService.count() == 5
    }

    void "test delete"() {
        Long metaDataId = setupData()

        expect:
        metaDataService.count() == 5

        when:
        metaDataService.delete(metaDataId)
        sessionFactory.currentSession.flush()

        then:
        metaDataService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        MetaData metaData = new MetaData()
        metaDataService.save(metaData)

        then:
        metaData.id != null
    }
}
