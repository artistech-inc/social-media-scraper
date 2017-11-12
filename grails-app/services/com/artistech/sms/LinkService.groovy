package com.artistech.sms

import grails.gorm.transactions.Transactional
import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@Transactional
class LinkService {

    def linkResolver() {
        runAsync {
            println "resolving links..."
            def ids = Link.executeQuery("select id from Link where resolved is null");
            try {
                ids.each {
                    Link link = Link.get(it)
                    try {
                        print link.id + ": "
                        println link.url
                        HttpURLConnection connection = (HttpURLConnection) new URL(link.url).openConnection()
                        connection.setInstanceFollowRedirects(false)
                        String location = link.url
                        def redirectedTo = []
                        while (!redirectedTo.contains(location) &&
                                connection.responseCode >= 300 && connection.responseCode < 400) {
                            redirectedTo.add(location)
                            location = connection.getHeaderField("location")
                            println location
                            connection = (HttpURLConnection) new URL(location).openConnection()
                            connection.setInstanceFollowRedirects(false)
                        }
                        link.resolved = location
                        link.save(flush: true)
                        println link.resolved
                    } catch (Exception ex) {
                        ex.printStackTrace()
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace()
            }
            println "done..."
        }
    }

    def linkExtractor() {
        runAsync {
            def originalTweets = Tweet.findAllByRetweeted_status(null)
            Parser p = new Parser()
            println "extracting links..."
            originalTweets.each {
                Tweet tweet = it
                def urls = p.parse(it.contents)
                println it.contents
                urls.each {
                    Link link = new Link(tweet: tweet, url: it).save()
                }
            }
        }
        println "done..."
    }

    def linkDownloader() {
        runAsync {
            def links = Link.findAllByContents(null)
            println "downloading links..."
            links.each {
                try {
                    URL url = new URL(it.url)
                    InputStream is = url.openStream()
                    it.contents = IOUtils.toString(is, "UTF-8")
                    it.save(flush: true)
                    print "Saved: "
                    println it.url
                    is.close()
                } catch (Exception ex) {
                    println ex
                }
            }
            println "done..."
        }
    }

    def linkScraper() {
        runAsync {
            println "scraping HTML..."
            def ids = Link.executeQuery( "select id from Link" );
            ids.each {
                Link link = Link.get(it)
                try {
                    if(link.contents != null && link.text == null) {
                        println link.url
                        Document doc = Jsoup.parse(link.contents)
                        String text = doc.body().text()
//                    link.text = text
//                    link.save(flush: true)
                    }
                } catch (Exception ex) {
                    println ex
                }
            }
            println "done..."
        }
    }
}
