package com.artistech.sms

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

//        delete "/$controller/$id(.$format)?"(action: 'delete')
//        get "/$controller(.$format)?"(action: 'index')
//        get "/$controller/$id(.$format)?"(action: 'show')
//        post "/$controller(.$format)?"(action: 'save')
//        put "/$controller/$id(.$format)?"(action: 'update')
//        patch "/$controller/$id(.$format)?"(action: 'patch')

        get "/restLink"(controller:"restLink", action:"index")
        get "/restLink/create"(controller:"restLink", action:"create")
        post "/restLink"(controller:"restLink", action:"save")
        get "/restLink/$id"(controller:"restLink", action:"show")
        get "/restLink/$id/edit"(controller:"restLink", action:"edit")
        put "/restLink/$id"(controller:"restLink", action:"update")
        delete "/restLink/$id"(controller:"restLink", action:"delete")

        get "/restTweet"(controller:"restTweet", action:"index")
        get "/restTweet/create"(controller:"restTweet", action:"create")
        post "/restTweet"(controller:"restTweet", action:"save")
        get "/restTweet/$id"(controller:"restTweet", action:"show")
        get "/restTweet/$id/edit"(controller:"restTweet", action:"edit")
        put "/restTweet/$id"(controller:"restTweet", action:"update")
        delete "/restTweet/$id"(controller:"restTweet", action:"delete")

        get "/restTweetUser"(controller:"restTweetUser", action:"index")
        get "/restTweetUser/create"(controller:"restTweetUser", action:"create")
        post "/restTweetUser"(controller:"restTweetUser", action:"save")
        get "/restTweetUser/$id"(controller:"restTweetUser", action:"show")
        get "/restTweetUser/$id/edit"(controller:"restTweetUser", action:"edit")
        put "/restTweetUser/$id"(controller:"restTweetUser", action:"update")
        delete "/restTweetUser/$id"(controller:"restTweetUser", action:"delete")

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
