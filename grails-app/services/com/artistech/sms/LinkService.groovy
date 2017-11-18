package com.artistech.sms

import grails.gorm.transactions.Transactional
import org.apache.commons.io.IOUtils
//import java.util.concurrent.atomic.AtomicInteger

@Transactional
class LinkService {

    def linkData(Link link) {
        System.setProperty("http.agent", "")
        try {
            String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
            HttpURLConnection connection = (HttpURLConnection) new URL(link.url).openConnection()
//            connection.setRequestProperty("User-Agent", userAgent);

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
//                connection.setRequestProperty("User-Agent", userAgent);
                connection.setInstanceFollowRedirects(false)
            }
            link.resolved = location
            println "resolved: " + link.resolved
            connection.disconnect()

            connection = (HttpURLConnection) new URL(link.resolved).openConnection()
            connection.setRequestProperty("User-Agent", userAgent);

            //success
            if (connection.responseCode >= 200 && connection.responseCode < 300) {
                InputStream is = connection.inputStream
                String contents = IOUtils.toString(is, "UTF-8")
                is.close()

                link.contents = contents
                println "downloaded: " + contents.length()
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
}