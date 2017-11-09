package com.artistech.sms

class TweetUser {

    String translator_type
    Boolean is_translation_enabled
    Integer utc_offset
    Integer statuses_count
    String follow_request_sent
    Boolean has_extended_profile
    String location
    Boolean profile_use_background_image
    Boolean contributors_enabled
    String profile_link_color
    String profile_image_url
    String notifications
    String screen_name
    Integer favourites_count
    String profile_background_image_url_https
    String profile_background_color
    String profile_banner_url
    String id_str
    String profile_background_image_url
    String description
    String lang
    Boolean default_profile
    Boolean profile_background_tile
    Boolean verified
    Integer friends_count
    //entities ?
    String url
//    String following
    String profile_image_url_https
    String profile_sidebar_fill_color
    String time_zone
    String name
    Boolean geo_enabled
    String profile_text_color
    Integer followers_count
    String profile_sidebar_border_color
    Boolean default_profile_image
    Boolean is_translator
    Boolean is_protected
    //use date here?
    String created_at
    Integer listed_count

    @Override
    String toString() {
        return id_str
    }

    static mapping = {
        profile_image_url type: 'text'
        profile_image_url_https type: 'text'
        url type: 'text'
        profile_banner_url type: 'text'
        profile_background_image_url type: 'text'
        profile_background_image_url_https type: 'text'
        profile_banner_url type: 'text'
    }

    static constraints = {
        translator_type nullable: true
        is_translation_enabled nullable: true
        utc_offset nullable: true
        statuses_count nullable: true
        follow_request_sent nullable: true
        has_extended_profile nullable: true
        location nullable: true
        profile_use_background_image nullable: true
        contributors_enabled nullable: true
        profile_link_color nullable: true
        profile_image_url nullable: true
        notifications nullable: true
        screen_name nullable: true
        favourites_count nullable: true
        profile_background_image_url_https nullable: true
        profile_background_color nullable: true
        profile_banner_url nullable: true
        id_str unique: true
        profile_background_image_url nullable: true
        description nullable: true
        lang nullable: true
        default_profile nullable: true
        profile_background_tile nullable: true
        verified nullable: true
        url nullable: true
        friends_count nullable: true
        profile_image_url_https nullable: true
        profile_sidebar_fill_color nullable: true
        time_zone nullable: true
        name nullable: true
        geo_enabled nullable: true
        profile_text_color nullable: true
        followers_count nullable: true
        profile_sidebar_border_color nullable: true
        default_profile_image nullable: true
        is_translator nullable: true
        is_protected nullable: true
        created_at nullable: true
        listed_count nullable: true
    }
}
