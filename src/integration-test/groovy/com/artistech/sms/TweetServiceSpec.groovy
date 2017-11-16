package com.artistech.sms

import grails.test.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TweetServiceSpec extends Specification {

    TweetService tweetService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Tweet(...).save(flush: true, failOnError: true)
        //new Tweet(...).save(flush: true, failOnError: true)
        //Tweet tweet = new Tweet(...).save(flush: true, failOnError: true)
        //new Tweet(...).save(flush: true, failOnError: true)
        //new Tweet(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //tweet.id
    }

    void "test get"() {
        setupData()

        expect:
        tweetService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Tweet> tweetList = tweetService.list(max: 2, offset: 2)

        then:
        tweetList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        tweetService.count() == 5
    }

    void "test delete"() {
        Long tweetId = setupData()

        expect:
        tweetService.count() == 5

        when:
        tweetService.delete(tweetId)
        sessionFactory.currentSession.flush()

        then:
        tweetService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Tweet tweet = new Tweet()
        tweetService.save(tweet)

        then:
        tweet.id != null
    }
}
