package com.artistech.sms

class JobController {

    def executorService

    def index() {

    }

    def exec() {
        JobThread jt = new JobThread()
        executorService.submit(jt)

        redirect (action: "index")
    }

    def downloadLinks() {
        DownloadThread jt = new DownloadThread()
        executorService.submit(jt)

        redirect (action: "index")
    }

    def resolveLinks() {
        println "resolving..."
        ShortenedResolver jt = new ShortenedResolver()

        println "submitted..."
        executorService.submit(jt)

        println "redirecting..."
        redirect(action: "index")
    }
}
