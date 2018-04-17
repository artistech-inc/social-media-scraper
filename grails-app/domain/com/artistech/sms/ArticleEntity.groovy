package com.artistech.sms

class ArticleEntity {

    def executorService

    String url
    String id_str
    String entity_name
    String mention_type
    
    String toString() {
        return id_str
    }

    static mapping = {
        url type: 'text'
        entity_name type: 'text'
        mention_type type: 'text'
        autowire true
    }

    static constraints = {
        id_str unique: true
    }

    def afterInsert(){}
}
