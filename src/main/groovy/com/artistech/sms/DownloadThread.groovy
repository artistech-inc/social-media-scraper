package com.artistech.sms

import org.apache.commons.io.IOUtils

class DownloadThread implements Runnable {

    @Override
    void run() {
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