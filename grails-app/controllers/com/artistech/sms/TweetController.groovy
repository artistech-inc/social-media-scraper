package com.artistech.sms

class TweetController {

    def bootStrapService
    def executorService
    def tweetService

    static scaffold = Tweet

    /**
     * Save the uploaded file to a temporary location, then process it in a separate thread.
     * Note: multiple exit points from this method depending on errors found.
     *
     */
    def upload(TweetCommand cmd) {
        if (cmd == null) {
            redirect view: 'index'
            return
        }
        cmd.userAgent = request.getHeader("User-Agent")
        if (cmd.hasErrors()) {
            // from the user not entering an email address and/or file to upload
            log.error "ERROR: ${cmd.errors}"
            log.debug "cmd has errors in upload"
            flash.error = "Enter an email address and file to upload (.json or .tar.gz)."
            respond(cmd.errors, view: 'create')
            return
        }
        def origFileName = cmd.tweetJsonFile.getOriginalFilename()
        
        // Save file to temporary location first, then process it
        def resultMap = bootStrapService.saveFile(cmd)
        
        File tempFile = resultMap.uploadedFile
        String tempFilePath = resultMap.fileWithPath

        if (tempFile != null) {
            // new thread for processing file
            log.debug "Starting new thread for processing tweets."
            executorService.submit {
                bootStrapService.loadFile(tempFile, cmd.emailAddress)
            }             
            flash.message = "Processing your file: ${tempFilePath}."

        } else {
            // report to user that there was an upload problem
            log.debug "No file returned from saveFile"
            flash.error = "Error uploading ${tempFilePath} -- Check your file and try again."
            redirect(action: "create")
            return
        }
        redirect action: "index"
       
    }

    /**
     *  uploadTweet -- a POST REST call to insert a JSON string
     *  
     *  Formerly a part of the index method, but that is now 
     *  serving to filter the results, so it needs to respond to both
     *  POST (from the filter form) and GET requests.
     *  Can either call upload with a TweetCommand object, above,
     *  or with no argument as a request.post call, and the tweet
     *  text should be in request.reader.text.
     *  Since this is a Controller, it can't overload a method and have another upload method
     *  with no arguments.
     *  
     **/
     def uploadTweet() {
        if(request.post) {
            //this is a REST call to insert a JSON string
            String tweet = request.reader.text
            bootStrapService.loadTweet(tweet)
            render "OK"
            return
        } else {
            render "Expecting POST call"
            return
        }
        
      
     }
    
    /**
     * 
     **/
    
    def index() {

        long offset = params.offset == null ? 0 : params.offset as long
        int max = params.max == null ? 10 : params.max as int
        
        // see what parameters are available
        log.debug "beginning of index, $params"
        
        def tweetsToShow
        def tweetCount
        // will contain Update View if user just updated settings on the form;
        // will contain null if the user didn't just click Update View 
        def cameFromForm = params._action_index
        // default sort type is by Date if user didn't pick popularity or credibility (tbd)
        def String sortType
        if (params.sortNumRetweets == "on") {
            sortType = "POPULARITY"
            // save in the session
            session.sortType = sortType
        } else {
            if (cameFromForm) {
                // nothing passed in as a parameter in the form, so user unselected this option
                // and need to save the default value in the session
                sortType = "DATE"
                session.sortType = sortType
            } else {
                // no sort type param AND not coming from the form action, so see if it's in the session
                sortType = session.sortType
                // if still null, user never selected Update View, so set to default value of DATE and save in session
                if (sortType == null) {
                    sortType = "DATE"
                    session.sortType = sortType
                }           
            }           
        }
        log.debug "selected sort type, may be from the session: ${sortType}"

        def String sortOrder = "DESC"
        // for now, only sort descending; save in session
        session.sortOrder = sortOrder
        
        // null (if user did not hit Update View), "", or the selected language
        def String selLang = params.languageSelect       
        // update user selections in the session to keep track of settings
        if (selLang == null) {
            // null can only mean that the form was not submitted and came here
            // from another post;
            // go with what was in the session, either null, "" or a value
            selLang = session.selectedLanguage
        } else {
            // reset the session selected language to this new one, may be "" or a value;
            // selected language was passed in as a parameter from the form
            session.selectedLanguage = selLang
        }
        log.debug "selected language, may be from the session: ${selLang}"
        log.debug "action index, if user submitted the form: ${params._action_index}"

        // on or null --  user could have unchecked it, and it would be null here
        def String originalTweet = params.original
        // if user did NOT submit the form, then use the settings from the session
        if (originalTweet == null) {
            // submitted the form with this box unchecked, OR came from another post so this is null;
            if (cameFromForm) {
                // save what the user selected in the form -- unselected the original tweet option
                // so this is null
                session.originalTweet = originalTweet
            } else {
                // didn't come from the form, so use what was in the session, either null or "on"
                originalTweet = session.originalTweet
            }
        } else {
            // reset the session; original tweet setting was passed in from the form
            session.originalTweet = originalTweet
        }
         log.debug "selected orig tweet, may be from the session: ${originalTweet}"
       
        // testing
        //tweetService.queryTweetContent("water")
        //queryForTweets(String sortOrder, String sortBy, long offsetNum, int maxNum,
        //                String selectedLang, String originalTweet )
        def resultMap = tweetService.queryForTweets(sortOrder, sortType, offset, max, 
                                                    selLang, originalTweet)
        tweetCount = resultMap.tweetCount
        tweetsToShow = resultMap.tweetList
        log.debug "in index, from queryForTweets: count: ${tweetCount}, size of tweet list: ${tweetsToShow.size()}"

        def langList = tweetService.queryForUniqueLanguages()
       
       // [tweetList: Tweet.list(offset: offset, max: max), tweetCount: Tweet.count()]
       // if sortType is Date, then this is the default and the user didn't select
       // anything to sort by, so set this to null
       def String sortNumRetweets = null
       // future use
       def String sortCredibility = null
       log.debug "sortType: ${sortType}"
       if (sortType.equals("DATE")) {
           sortNumRetweets = null
           sortCredibility = null
       } else if (sortType.equals("POPULARITY")) {
           sortNumRetweets = "on"
           sortCredibility = null
       } else {
           sortNumRetweets = null
           sortCredibility = "on"          
       }
       log.debug "sortNumRetweets: ${sortNumRetweets}"
       [tweetList: tweetsToShow, tweetCount: tweetCount, languages: langList, 
            origTweet: originalTweet, selectedLang: selLang, sortNumRetweets: sortNumRetweets]
    }
    
    

     
    /**
    * TESTING 
    * Called from button to Update view with filters
    * Redirect to index method, so that it won't be a request.post REST call;
    * If you forward instead, that's a post call to index()
    *
    **/
   
    def updateFilters() {
        log.debug "updateFilters, request.post: ${request.post}"
        
        log.debug "In updateFilters, copying params: ${params}"
        //def paramCopy = params
        //log.debug "    the copy: ${paramCopy}"
        
        //redirect action: "index", params: ${params} 
        redirect action: "index", params: [sortOriginal: ${params.sortOriginal}]
    }
}
