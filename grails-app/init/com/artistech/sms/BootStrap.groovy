package com.artistech.sms

class BootStrap {

    def bootStrapService

    def init = { servletContext ->



//        ContentTable ct = new ContentTable("jdbc:mysql://lespaul:3306/smsProd?autoReconnect=true&useSSL=false", "sms", "sms", "com.mysql.jdbc.Driver")
//        ct.getIds("select id from search_file", { id ->
//            ct.getContents("select contents from search_file where id = " + id, { contents ->
//                println contents
//            })
//            println id
//        })
    }
    def destroy = {
    }
}
