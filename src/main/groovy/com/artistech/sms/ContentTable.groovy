package com.artistech.sms
import groovy.sql.Sql


class ContentTable {

    def sql

    ContentTable(String connStr, String user, String pass, String driver) {
        sql = Sql.newInstance( connStr, user, pass, driver )
    }

    def getIds(String query, Closure cl) {
        sql.eachRow(query) {
            cl(it.id)
        }
    }

    def getContents(String query, Closure cl) {
        sql.eachRow(query) {
            cl(it.contents)
        }
    }
}