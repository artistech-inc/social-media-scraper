package com.artistech.sms

class JobController {

    def executorService
    def linkService

    def index() {

    }

    def exec() {
        linkService.linkExtractor()

        redirect (action: "index")
    }

    def downloadLinks() {
        linkService.linkDownloader()

        redirect (action: "index")
    }

    def resolveLinks() {
        println "resolving..."
        linkService.linkResolver()

        println "redirecting..."
        redirect(action: "index")
    }
}
