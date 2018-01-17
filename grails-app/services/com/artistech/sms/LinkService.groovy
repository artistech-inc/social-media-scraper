package com.artistech.sms

import grails.gorm.transactions.Transactional
import org.apache.commons.io.IOUtils

@Transactional
class LinkService {

    def linkData(Link link) {
        System.setProperty("http.agent", "")
        try {
            String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
            HttpURLConnection connection

            //if the url has been specified as resloved, don't resolve again.
            if(link.resolved == null) {
                connection = (HttpURLConnection) new URL(link.url).openConnection()
                connection.setRequestProperty("User-Agent", "");

                connection.setInstanceFollowRedirects(false)
                String location = link.url
                def redirectedTo = []
                //redirect
                while (!redirectedTo.contains(location) &&
                        connection.responseCode >= 300 && connection.responseCode < 400) {
                    redirectedTo.add(location)
                    location = connection.getHeaderField("location")
                    connection.disconnect()
                    connection = (HttpURLConnection) new URL(location).openConnection()
                    connection.setRequestProperty("User-Agent", "");
                    connection.setInstanceFollowRedirects(false)
                }
                link.resolved = location
                println "resolved [" + link.id + "]: " + link.url + " to " + link.resolved
                connection.disconnect()
            }

            //download data, spoof user-agent to gain access to all HTML
            connection = (HttpURLConnection) new URL(link.resolved).openConnection()
            connection.setRequestProperty("User-Agent", userAgent);

            //success
            if (connection.responseCode >= 200 && connection.responseCode < 300) {
                InputStream is = connection.inputStream
                String contents = IOUtils.toString(is, "UTF-8")
                is.close()

                link.contents = contents
                println "downloaded [" + link.id + "]: " + link.resolved + " (" + contents.length() + ")"
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
