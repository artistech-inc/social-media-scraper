package com.artistech.sms

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class LinkController {

    static scaffold = Link

    def show() {
        Link link = Link.get(params.id)
        respond ([url: link.url, resolved: link.resolved, tweetid: link.tweet.id_str, downloaded: link.contents != null])
    }

    def contents() {
        Link link = Link.get(params.id)
        Document doc = Jsoup.parse(link.contents)
        String contents = doc.body().text()
        respond ([url: link.url, resolved: link.resolved, tweetid: link.tweet.id_str, contents: contents])
    }
}
