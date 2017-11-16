package com.artistech.sms

class TweetController {

    def bootStrapService
    def executorService

    static scaffold = Tweet

    def upload(TweetCommand cmd) {
        if (cmd == null) {
            redirect view: 'index'
            return
        }

        if (cmd.hasErrors()) {
            println cmd.errors
            respond(cmd.errors, view: 'create')
            return
        }

        executorService.submit {
            bootStrapService.loadFile(cmd)
        }

        redirect action: "index"
    }

    def create() {

    }
}
