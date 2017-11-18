package com.artistech.sms

class TweetUserController {

    static scaffold = TweetUser

    def index() {
        long offset = params.offset == null ? 0 : params.offset as long
        int max = params.max == null ? 10 : params.max as int
        [tweetUserList: TweetUser.list(offset: offset, max: max), tweetUserCount: TweetUser.count()]
    }

}
