package com.artistech.sms

class LinkController {

    static scaffold = Link

    def show() {
        Link link = Link.get(params.id)
        respond ([url: link.url, resolved: link.resolved, tweetid: link.tweet.id_str, downloaded: link.contents != null])
    }
}
