package com.artistech.sms

class JobController {

    def index() {

    }

    def exec() {
        LinkService ls = new LinkService()
        ls.linkExtractor()

        redirect (action: "index")
    }

    def downloadLinks() {
        LinkService ls = new LinkService()
        ls.linkDownloader()

        redirect (action: "index")
    }

    def resolveLinks() {
        println "resolving..."
        LinkService ls = new LinkService()
        ls.linkResolver()

        println "redirecting..."
        redirect(action: "index")
    }
}
