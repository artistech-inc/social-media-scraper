package com.artistech.sms

class AutoPhrase {

    def executorService

    String id_str
    Float rank
    String entity_name
    
    String toString() {
        return id_str
    }

    static mapping = {
        rank type: 'float'
        entity_name type: 'text'
        autowire true
    }

    static constraints = {
        id_str unique: true
    }

    def afterInsert(){}
}
