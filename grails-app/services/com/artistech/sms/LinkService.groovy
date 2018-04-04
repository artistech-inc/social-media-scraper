package com.artistech.sms

import grails.gorm.transactions.Transactional
import org.apache.commons.io.IOUtils
import grails.plugins.rest.client.RestBuilder

@Transactional
class LinkService {

    //Perhaps try to set this to whatever the user used?  But this is sufficient for now.
    static String USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0"
    static final int TIMEOUT = 1000

    def linkData(Link link) {
        System.setProperty("http.agent", "")
        HttpURLConnection connection
        try {
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
                    connection.connectTimeout = TIMEOUT
                    connection.readTimeout = TIMEOUT
                    location = connection.getHeaderField("location")
                    connection.disconnect()

                    connection = (HttpURLConnection) new URL(location).openConnection()
                    connection.setRequestProperty("User-Agent", "");
                    connection.setInstanceFollowRedirects(false)

                }
                link.resolved = location
                connection.disconnect()
                log.debug "resolved [${link.id}]: ${link.url} to ${link.resolved}"
                try {
                    RestBuilder rest = new RestBuilder()
                    def body_str = 'url=' + link.resolved
                    log.warn body_str.toString()
                    def resp = rest.post('http://localhost:5000/new-tweet-link') {
                        contentType "application/x-www-form-urlencoded"
                        body body_str.toString()
                    }
                    log.warn resp.text.toString()
                } catch(Exception e) {
                    log.warn "got rest error"
                    log.warn e.getMessage()
                }
            }

            if(link.contents == null) {
                //download data, spoof user-agent to gain access to all HTML
                connection = new URL(link.url).openConnection()
                connection.connectTimeout = TIMEOUT
                connection.readTimeout = TIMEOUT
                connection.setRequestProperty("User-Agent", USER_AGENT);

                //success
                if (connection.responseCode >= 200 && connection.responseCode < 300) {
                    InputStream is = connection.inputStream
                    String contents = IOUtils.toString(is, "UTF-8")
                    is.close()

                    link.contents = contents
                    log.debug "downloaded [${link.id}]: (${contents.length()})"
                }
                connection.disconnect()
                connection = null;
            }
        } catch (java.io.IOException ex) {
            log.warn "${ex.message}"
        } catch (java.io.FileNotFoundException ex) {
            log.warn "${ex.message}"
        } catch (java.net.MalformedURLException ex) {
            log.warn "${ex.message}"
        } catch (javax.net.ssl.SSLHandshakeException ex) {
            log.warn "${ex.message}"
        } catch (javax.net.ssl.SSLProtocolException ex) {
            log.warn "${ex.message}"
        } catch (java.net.UnknownHostException ex) {
            log.warn "${ex.message}"
        } catch (java.net.NoRouteToHostException ex){
            log.warn "${ex.message}"
        } catch (java.net.ConnectException ex) {
            log.warn "${ex.message}"
        } catch (Exception ex) {
            log.error "Unexpected Error: ${ex.message}", ex
        } finally {
            if (connection != null) {
                connection.disconnect()
            }
        }

        return link
    }
}
