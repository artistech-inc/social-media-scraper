package com.artistech.sms

import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper

import java.text.SimpleDateFormat

@Transactional
class BootStrapService {

    def loadTweet(Map map) {
        String id_str = map["id_str"]
        Tweet existing = Tweet.findById_str(id_str)
        if(existing != null) {
            return existing
        }
        Tweet tweet = new Tweet()

        tweet.contents = map["text"]
        map.each { key, value ->
            if (key != "id" &&
                    key != "user" &&
                    key != "retweeted_status" &&
                    key != "metadata" &&
                    key != "created_at") {
                try {
                    if (tweet.hasProperty(key)) {
                        tweet.setProperty(key, value)
//                        println tweet.getProperty(key)
                    } else {
                        if (!key in ["coordinates", "entities"]) {
                            println "Add Tweet Property: "
                            println key
                            println value
                        }
                    }
                } catch (groovy.lang.MissingPropertyException ex) {
                    println ex.message
                    println key
                    println value
                } catch (org.codehaus.groovy.runtime.typehandling.GroovyCastException ex) {
                    println ex.message
                    println key
                    println value
                }
            } else if(key == "created_at") {
                //Sun Feb 01 17:43:12 +0000 2009"
                SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
                Date date = parser.parse(value);
                tweet.created_at = date
            } else if(key == "user") {
                TweetUser tu = loadUser(value)
                tweet.user = tu
                //find/create user
            } else if(key == "retweeted_status") {
                Tweet rt = loadTweet(value)
                tweet.retweeted_status = rt
                //find/create tweet
            } else if (key == "metadata") {
                //create metadata
            }
        }
        tweet.save(failOnError: true)
        return tweet
    }

    def loadUser(Map map) {
        String id_str = map["id_str"]
        TweetUser existing = TweetUser.findById_str(id_str)
        if(existing != null) {
            return existing
        }

        TweetUser user = new TweetUser()
        map.each { key, value ->
            if (key != "id" &&
                    key != "user" &&
                    key != "retweeted_status" &&
                    key != "metadata" &&
                    key != "created_at") {
                try {
                    if (user.hasProperty(key)) {
                        user.setProperty(key, value)
                    } else {
                        if(!key in ["coordinates", "entities"]) {
                            println "Add User Property: "
                            println key
                            println value
                        }
                    }
                } catch(groovy.lang.MissingPropertyException ex) {
                    println ex.message
                    println key
                    println value
                } catch(org.codehaus.groovy.runtime.typehandling.GroovyCastException ex) {
                    println ex.message
                    println key
                    println value
                }
            } else if(key == "created_at") {
                //Thu Mar 20 18:34:23 +0000 2014"
                SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
                Date date = parser.parse(value);
                user.created_at = date
            } else if(key == "user") {
                TweetUser tu = loadUser(value)
                //find/create user
            } else if(key == "retweeted_status") {
                //find/create tweet
            } else if (key == "metadata") {
                //create metadata
            }
        }
        user.save(failOnError: true)
        return user
    }

    def loadFile(File file) {
        JsonSlurper slurper = new JsonSlurper();
        file.eachLine( {
            def map = slurper.parseText(it)
            loadTweet(map)
        })
    }
}
