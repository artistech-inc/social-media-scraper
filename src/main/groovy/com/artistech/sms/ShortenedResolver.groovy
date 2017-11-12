package com.artistech.sms

class ShortenedResolver implements Runnable {

    @Override
    void run() {
        try {
            println "resolving links..."
            def ids = Link.executeQuery("select id from Link where resolved is null");
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
        catch(Exception ex) {
            ex.printStackTrace()
        } finally {
            println "done..."
        }
    }

}