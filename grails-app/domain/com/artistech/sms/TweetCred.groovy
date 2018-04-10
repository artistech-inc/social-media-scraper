package com.artistech.sms

class TweetCred {

    def executorService

    String url
    String id_str
    Float reliable_style
    Float biased_style
    Float subjectivity_title
    Float subjectivity_body
    
    String toString() {
        return id_str
    }

    static mapping = {
        url type: 'text'
        reliable_style type: 'float'
        biased_style type: 'float'
        subjectivity_title type: 'float'
        subjectivity_body type: 'float'
        autowire true
    }

    static constraints = {
        id_str unique: true
    }

    def afterInsert(){}
}
