package com.artistech.sms

class LinkController {

    static scaffold = Link

    def show() {
        Link link = Link.get(params.id)
        [url: link.url, tweetid: link.tweet.id_str, downloaded: link.contents != null]
    }
}
