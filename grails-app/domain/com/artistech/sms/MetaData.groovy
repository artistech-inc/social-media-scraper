package com.artistech.sms

class MetaData {

    String name
    String value

    static constraints = {
        name nullable: false
        value nullable: false
    }
}
