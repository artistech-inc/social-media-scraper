package com.artistech.sms

import grails.gorm.transactions.Transactional
import org.apache.commons.io.IOUtils
//import java.util.concurrent.atomic.AtomicInteger

@Transactional
class LinkService {
//    def executorService
//
//    def linkResolver() {
//        runAsync {
//            println "resolving links..."
//            def ids = Link.executeQuery("select id from Link where resolved is null");
//            AtomicInteger counter = new AtomicInteger(0);
//            try {
//                def map = Collections.synchronizedMap(new HashMap());
//                ids.each {
//                    Runnable r = new Runnable() {
//
//                        Link link = Link.get(it)
//
//                        @Override
//                        void run() {
//                            try {
//                                HttpURLConnection connection = (HttpURLConnection) new URL(link.url).openConnection()
//                                connection.setInstanceFollowRedirects(false)
//                                String location = link.url
//                                def redirectedTo = []
//                                while (!redirectedTo.contains(location) &&
//                                        connection.responseCode >= 300 && connection.responseCode < 400) {
//                                    redirectedTo.add(location)
//                                    location = connection.getHeaderField("location")
//                                    connection = (HttpURLConnection) new URL(location).openConnection()
//                                    connection.setInstanceFollowRedirects(false)
//                                }
//                                map[it] = location
//                                println "Setting [" + it + "] - " + map[it]
//                            } catch (java.net.MalformedURLException ex) {
//                            } catch (javax.net.ssl.SSLHandshakeException ex) {
//                            } catch (javax.net.ssl.SSLProtocolException ex) {
//                            } catch (java.net.UnknownHostException ex) {
//                            } catch (java.net.NoRouteToHostException ex){
//                            } catch (java.net.ConnectException ex) {
//                            } catch (Exception ex) {
//                                ex.printStackTrace()
//                            } finally {
//                                counter.incrementAndGet()
//                            }
//                        }
//                    }
//                    executorService.submit(r)
//                }
//
//                while(counter.get() != ids.size()) {
//                    println counter.get() + " / " + ids.size()
//                    try {
//                        def map2 = new HashMap(map)
//                        def toRemove = []
//
//                        map2.each { key, value ->
//                            toRemove.add(key)
//
//                            Link link = Link.get(key)
//                            link.resolved = value
//                            println "Saving: [" + link.id + "] - " + link.resolved
//                            link.save(flush: true)
//                        }
//                        toRemove.each {
//                            map.remove(it)
//                        }
//                        Thread.sleep(500)
//                    } catch (Exception ex) {
//                        ex.printStackTrace()
//                    }
//                }
//
//                map.each { key, value ->
//                    Link link = Link.get(key)
//                    link.resolved = value
//                    println "Saving: [" + link.id + "] - " + link.resolved
//                    link.save(flush: true)
//                }
//
//            }
//            catch (Exception ex) {
//                ex.printStackTrace()
//            }
//            println "done..."
//        }
//    }
//
//    def linkResolver(Link link) {
//        String resolved = null
//        try {
//            HttpURLConnection connection = (HttpURLConnection) new URL(link.url).openConnection()
//            connection.setInstanceFollowRedirects(false)
//            println "resolving: " + link.url
//            String location = link.url
//            def redirectedTo = []
//            while (!redirectedTo.contains(location) &&
//                    connection.responseCode >= 300 && connection.responseCode < 400) {
//                redirectedTo.add(location)
//                location = connection.getHeaderField("location")
//                connection.disconnect()
//                connection = (HttpURLConnection) new URL(location).openConnection()
//                connection.setInstanceFollowRedirects(false)
//            }
//            connection.disconnect()
//            resolved = location
//        } catch (java.net.MalformedURLException ex) {
//        } catch (javax.net.ssl.SSLHandshakeException ex) {
//        } catch (javax.net.ssl.SSLProtocolException ex) {
//        } catch (java.net.UnknownHostException ex) {
//        } catch (java.net.NoRouteToHostException ex){
//        } catch (java.net.ConnectException ex) {
//        } catch (Exception ex) {
//            ex.printStackTrace()
//        } finally {
//        }
//        println "resolved: " + resolved != null ? resolved : "null"
//        return resolved
//    }

    def linkData(Link link) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(link.url).openConnection()
            connection.setInstanceFollowRedirects(false)
            println "resolving: " + link.url
            String location = link.url
            def redirectedTo = []
            //redirect
            while (!redirectedTo.contains(location) &&
                    connection.responseCode >= 300 && connection.responseCode < 400) {
                redirectedTo.add(location)
                location = connection.getHeaderField("location")
                connection.disconnect()
                connection = (HttpURLConnection) new URL(location).openConnection()
                connection.setInstanceFollowRedirects(false)
            }
            link.resolved = location
            println "resolved: " + location

            //success
            if (connection.responseCode >= 200 && connection.responseCode < 300) {
                InputStream is = connection.inputStream
                String contents = IOUtils.toString(is, "UTF-8")
                is.close()

                println "downloaded: " + contents.length()

                link.contents = contents
            }
            connection.disconnect()
        } catch (java.io.IOException ex) {
        } catch (java.io.FileNotFoundException ex) {
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

        return link
    }
//
//    def linkDownloader(Link link) {
//        String contents = null
//        try {
//            println "downloading: " + link.url
//            URL url = new URL(link.url)
//            InputStream is = url.openStream()
//            contents = IOUtils.toString(is, "UTF-8")
//            is.close()
//        } catch (Exception ex) {
//            println ex
//        }
//        println "downloaded: " + contents != null ? contents.length() : 0
//        return contents
//    }
}
