package com.artistech.sms

import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.grails.core.util.StopWatch

import java.text.SimpleDateFormat

@Transactional
class BootStrapService {

    def executorService
    def mailService

    def loadTweet(Map map) {
        String id_str = map["id_str"]
        Tweet existing = Tweet.findById_str(id_str)
        if(existing != null) {
            return existing
        }
        Tweet tweet = new Tweet()
        def links = []

        tweet.contents = map["text"]
        map.each { key, value ->
            if (key != "id" &&
                    key != "user" &&
                    key != "retweeted_status" &&
                    key != "metadata" &&
                    key != "created_at" &&
                    key != "entities") {
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
            } else if (key == "entities") {
                //load data into map
                map[key]["urls"].each {
                    Link link = new Link()
                    link.url = it["url"]
                    link.tweet = tweet
                    links.add(link)

                    //comment out to custom resolve...
                    //link.resolved = it["expanded_url"]
                }
            }
        }
        tweet.save(failOnError: true, flush: true)
        links.each {
            it.save(failOnError: true, flush: true)
        }
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
        user.save(failOnError: true, flush: true)
        return user
    }

    def loadTweet(String tweet) {
        JsonSlurper slurper = new JsonSlurper();
        println tweet
        def map = slurper.parseText(tweet)
        loadTweet(map)
    }

    def loadFile(TweetCommand cmd) {
        String emailAddress = cmd.emailAddress

        try {
            StopWatch sw = new StopWatch()
            sw.start()
            String fileName = cmd.tweetJsonFile.originalFilename.toLowerCase()

            if (fileName.endsWith(".json")) {
                println "Reading: " + cmd.tweetJsonFile.originalFilename
                def is = cmd.tweetJsonFile.inputStream
                InputStreamReader sr = new InputStreamReader(is)

                if (mailService != null) {
                    executorService.submit({
                        mailService.sendMail {
                            to emailAddress
                            subject "Import Start"
                            body 'Import Start'
                        }
                    })
                }

                String str = sr.readLine()
                JsonSlurper slurper = new JsonSlurper();
                while (str != null) {
                    def map = slurper.parseText(str)
                    loadTweet(map)
                    str = sr.readLine()
                }

                println "done reading input file!"
            } else if (fileName.endsWith(".tar.gz")) {
                def is = cmd.tweetJsonFile.inputStream
                BufferedInputStream bin = new BufferedInputStream(is)

                if (mailService != null) {
                    executorService.submit({
                        mailService.sendMail {
                            to emailAddress
                            subject "Import Start"
                            body 'Import Start'
                        }
                    })
                }

                GzipCompressorInputStream gzIn = new GzipCompressorInputStream(bin)
                TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn)

                TarArchiveEntry entry = null;

                /** Read the tar entries using the getNextEntry method **/

                while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {

                    println "Extracting: " + entry.getName()

                    /** If the entry is a directory, create the directory. **/

                    if (entry.isDirectory()) {
                    }
                    /**
                     * If the entry is a file,write the decompressed file to the disk
                     * and close destination stream.
                     **/
                    else {
                        int count;
                        InputStreamReader sr = new InputStreamReader(tarIn)
                        String str = sr.readLine()
                        JsonSlurper slurper = new JsonSlurper();
                        while (str != null) {
                            def map = slurper.parseText(str)
                            loadTweet(map)
                            str = sr.readLine()
                        }
                    }
                }

                /** Close the input stream **/

                tarIn.close();
                println "done reading input file!"
            }
            sw.stop()
            if(mailService != null) {
                executorService.submit({
                    sendMail {
                        to emailAddress
                        subject "Import Complete"
                        body 'Import Complete: ' + sw.toString()
                    }
                })
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace()
            executorService.submit({
                sendMail {
                    to emailAddress
                    subject fnfe.message
                    body fnfe.toString()
                }
            })
        }
    }
}
