package com.artistech.sms

import grails.validation.Validateable
import org.springframework.web.multipart.MultipartFile

class TweetCommand implements Validateable {
    MultipartFile tweetJsonFile

    static constraints = {
        tweetJsonFile  validator: { val, obj ->
            if ( val == null ) {
                return false
            }
            if ( val.empty ) {
                return false
            }

            ['.json', '.tar.gz'].any { extension ->
                val.originalFilename?.toLowerCase()?.endsWith(extension)
            }
        }
    }
}
