package com.artistech.sms

class TweetCred {

    def executorService

    String contents
    String id_str
    
    String toString() {
        return id_str
    }

    static mapping = {
        contents type: 'text'
        autowire true
    }

    static constraints = {
        id_str unique: true
    }

    def afterInsert(){}
}
