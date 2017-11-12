package com.artistech.sms

import grails.gorm.transactions.Transactional
import org.apache.commons.io.IOUtils
import java.util.concurrent.atomic.AtomicInteger

@Transactional
class LinkService {
    def executorService

    def linkResolver() {
        runAsync {
            println "resolving links..."
            def ids = Link.executeQuery("select id from Link where resolved is null");
            AtomicInteger counter = new AtomicInteger(0);
            try {
                def map = Collections.synchronizedMap(new HashMap());
                ids.each {
                    Runnable r = new Runnable() {

                        Link link = Link.get(it)

                        @Override
                        void run() {
                            try {
                                HttpURLConnection connection = (HttpURLConnection) new URL(link.url).openConnection()
                                connection.setInstanceFollowRedirects(false)
                                String location = link.url
                                def redirectedTo = []
                                while (!redirectedTo.contains(location) &&
                                        connection.responseCode >= 300 && connection.responseCode < 400) {
                                    redirectedTo.add(location)
                                    location = connection.getHeaderField("location")
                                    connection = (HttpURLConnection) new URL(location).openConnection()
                                    connection.setInstanceFollowRedirects(false)
                                }
                                map[it] = location
                                println "Setting [" + it + "] - " + map[it]
                            } catch (java.net.MalformedURLException ex) {
                            } catch (javax.net.ssl.SSLHandshakeException ex) {
                            } catch (javax.net.ssl.SSLProtocolException ex) {
                            } catch (java.net.UnknownHostException ex) {
                            } catch (java.net.NoRouteToHostException ex){
                            } catch (java.net.ConnectException ex) {
                            } catch (Exception ex) {
                                ex.printStackTrace()
                            } finally {
                                counter.incrementAndGet()
                            }
                        }
                    }
                    executorService.submit(r)
                }

                while(counter.get() != ids.size()) {
                    try {
                        def map2 = new HashMap(map)
                        def toRemove = []

                        map2.each { key, value ->
                            toRemove.add(key)

                            Link link = Link.get(key)
                            link.resolved = value
                            println "Saving: [" + link.id + "] - " + link.resolved
                            link.save(flush: true)
                        }
                        toRemove.each {
                            map.remove(it)
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace()
                    }
                }

                map.each { key, value ->
                    Link link = Link.get(key)
                    link.resolved = value
                    println "Saving: [" + link.id + "] - " + link.resolved
                    link.save(flush: true)
                }

            }
            catch (Exception ex) {
                ex.printStackTrace()
            }
            println "done..."
        }
    }

    def linkResolver(Link link) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(link.url).openConnection()
            connection.setInstanceFollowRedirects(false)
            String location = link.url
            def redirectedTo = []
            while (!redirectedTo.contains(location) &&
                    connection.responseCode >= 300 && connection.responseCode < 400) {
                redirectedTo.add(location)
                location = connection.getHeaderField("location")
                connection = (HttpURLConnection) new URL(location).openConnection()
                connection.setInstanceFollowRedirects(false)
            }
            link.resolved = location
            println "Saving: [" + link.id + "] - " + link.resolved
            link.save(flush: true)
        } catch (java.net.MalformedURLException ex) {
        } catch (javax.net.ssl.SSLHandshakeException ex) {
        } catch (javax.net.ssl.SSLProtocolException ex) {
        } catch (java.net.UnknownHostException ex) {
        } catch (java.net.NoRouteToHostException ex){
        } catch (java.net.ConnectException ex) {
        } catch (Exception ex) {
            ex.printStackTrace()
        } finally {
        }
    }

    def linkDownloader(Link link) {
        try {
            URL url = new URL(link.url)
            InputStream is = url.openStream()
            link.contents = IOUtils.toString(is, "UTF-8")
            link.save(flush: true)
            print "Saved: "
            println link.url
            is.close()
        } catch (Exception ex) {
            println ex
        }
    }

//    def linkScraper() {
//        runAsync {
//            println "scraping HTML..."
//            def ids = Link.executeQuery( "select id from Link" );
//            ids.each {
//                Link link = Link.get(it)
//                try {
//                    if(link.contents != null && link.text == null) {
//                        println link.url
//                        Document doc = Jsoup.parse(link.contents)
//                        String text = doc.body().text()
////                    link.text = text
////                    link.save(flush: true)
//                    }
//                } catch (Exception ex) {
//                    println ex
//                }
//            }
//            println "done..."
//        }
//    }
}
