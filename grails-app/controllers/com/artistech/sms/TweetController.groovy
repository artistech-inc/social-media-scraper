package com.artistech.sms

import org.springframework.web.multipart.MultipartFile

class TweetController {
    def bootStrapService

    static scaffold = Tweet

    def upload(TweetCommand cmd) {
        if (cmd == null) {
            notFound()
            return
        }

        if (cmd.hasErrors()) {
            println cmd.errors
            respond(cmd.errors, view: 'create')
            return
        }

        runAsync {
            bootStrapService.loadFile(cmd)
        }

        redirect action: "index"
    }

    def create() {

    }
}
