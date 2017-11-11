package com.artistech.sms

class JobController {

    def executorService

    def index() {

    }

    def exec() {
        JobThread jt = new JobThread()
        jt.service = executorService
        executorService.submit(jt)

        redirect (action: "index")
    }

    def downloadLinks() {
        DownloadThread jt = new DownloadThread()
        jt.service = executorService
        executorService.submit(jt)

        redirect (action: "index")
    }
}
