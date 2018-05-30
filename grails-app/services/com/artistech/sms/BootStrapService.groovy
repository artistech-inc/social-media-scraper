package com.artistech.sms

import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.lang.exception.ExceptionUtils
import org.grails.core.util.StopWatch

import java.text.SimpleDateFormat

@Transactional
class BootStrapService {

    def executorService
    def mailService

    def loadTweet(Map map) {
        String id_str = map["id_str"]
        Tweet existing = Tweet.findById_str(id_str)
        if(existing != null) {
            return existing
        }
        Tweet tweet = new Tweet()
        def links = []
        // hash of urls that were found in the text and in the entities tag;
        // should only store 1 link
        Set<String> urlsSet = new HashSet<>()

        tweet.contents = map["text"]
        log.debug "new tweet: ${map["id"]}, contents: ${tweet.contents}"
        map.each { key, value ->
            if (key != "id" &&
                    key != "user" &&
                    key != "retweeted_status" &&
                    key != "metadata" &&
                    key != "created_at" &&
                    key != "entities") {
                try {
                    if (tweet.hasProperty(key)) {
                        tweet.setProperty(key, value)
                    } else {
                        if (!key in ["coordinates", "entities"]) {
                            log.debug "Add Tweet Property: ${key}: ${value}"
                        }
                    }
                } catch (groovy.lang.MissingPropertyException ex) {
                    log.warn "${ex.message}: ${key}: ${value}", ex
                } catch (org.codehaus.groovy.runtime.typehandling.GroovyCastException ex) {
                    log.warn "${ex.message}: ${key}: ${value}", ex
                }
            } else if(key == "created_at") {
                //Sun Feb 01 17:43:12 +0000 2009"
                SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
                Date date = parser.parse(value);
                tweet.created_at = date
            } else if(key == "user") {
                TweetUser tu = loadUser(value)
                tweet.user = tu
                //find/create user
            } else if(key == "retweeted_status") {
                Tweet rt = loadTweet(value)
                tweet.retweeted_status = rt
                //find/create tweet
            } else if (key == "metadata") {
                //create metadata
            } else if (key == "entities") {
                def entities = map[key]
                def urls = entities["urls"]
                log.debug "tweet with entities ${map["id"]}, entities field: ${entities}"
                log.debug "     content: ${map["text"]}"
                log.debug "     urls: " + urls
                //Set<String> urlsSet = new HashSet<>()
                if(urls != null) {
                    urls.each {
                        if(!urlsSet.contains(it["url"])) {
                            urlsSet.add(it["url"])

                            Link l = new Link()
                            l.url = it["url"]
                            l.tweet = tweet
                            links.add(l)
                        }
                    }
                } else {
                    log.debug "***** no urls specified in entities tag, checking text later"
                }
            }
        }
        // check to see if links were found; if not, check the text
        // just in case there are links but no entities field in the tweet
        if (links.size() == 0 && tweet.contents != null) {
            log.debug "NO LINKS, checking text, ${tweet.contents}"
            // Parser is found in com.artistech.sms
            Parser p = new Parser()
            def parsedUrls = p.parse(tweet.contents)
            
            parsedUrls.each {
                log.debug "     Found URL in text: ${it}"
                if(!urlsSet.contains(it)) {
                    log.debug "       adding url to set"
                    urlsSet.add(it)
                    Link textLink = new Link()
                    textLink.url = it
                    textLink.tweet = tweet
                    links.add(textLink)
                }
                
            }          
        }
        log.debug "Found ${links.size()} links in the tweet"
        tweet.save(failOnError: true, flush: true)
        links.each {
            it.save(failOnError: true, flush: true)
        }
        return tweet
    }

    def loadUser(Map map) {
        String id_str = map["id_str"]
        TweetUser existing = TweetUser.findById_str(id_str)
        if(existing != null) {
            return existing
        }

        TweetUser user = new TweetUser()
        map.each { key, value ->
            if (key != "id" &&
                    key != "user" &&
                    key != "retweeted_status" &&
                    key != "metadata" &&
                    key != "created_at") {
                try {
                    if (user.hasProperty(key)) {
                        user.setProperty(key, value)
                    } else {
                        if(!key in ["coordinates", "entities"]) {
                            log.debug "Add User Property: ${key}: ${value}"
                        }
                    }
                } catch(groovy.lang.MissingPropertyException ex) {
                    log.warn "${ex.message}: ${key}: ${value}", ex
                } catch(org.codehaus.groovy.runtime.typehandling.GroovyCastException ex) {
                    log.warn "${ex.message}: ${key}: ${value}", ex
                }
            } else if(key == "created_at") {
                //Thu Mar 20 18:34:23 +0000 2014"
                SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
                Date date = parser.parse(value);
                user.created_at = date
            } else if(key == "user") {
                TweetUser tu = loadUser(value)
                //find/create user
            } else if(key == "retweeted_status") {
                //find/create tweet
            } else if (key == "metadata") {
                //create metadata
            }
        }
        user.save(failOnError: true, flush: true)
        return user
    }

    def loadTweet(String tweet) {
        JsonSlurper slurper = new JsonSlurper();
        log.debug "Tweet: ${tweet}"
        def map = slurper.parseText(tweet)
        loadTweet(map)
    }

    /**
    * Called from -- TweetController.Upload
    * Save the uploaded file in a temporary location for processing later.
    * Uses "upload_", System.getProperty("java.io.tmpdir"), and the datehourminsec appended
    *       to the uploaded file name.
    * Returns Map of:
    *           File uploadedFile -- uploaded file saved in temporary location or NULL if there was an error
    *           String fileWithPath -- full path of uploaded file in temporary location
    **/
    def saveFile(TweetCommand cmd) {   
        // see how long it takes to save the file
        StopWatch sw = new StopWatch()
        sw.start()
        
        String fileName = cmd.tweetJsonFile.getOriginalFilename().toLowerCase()  
        log.debug "Saving file ${fileName}"
        
        Date theDate = new Date()
        String fileWithPath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + 
                                "upload_" + theDate.format("MMddyyHHmmss") + "_" + 
                                cmd.tweetJsonFile.getOriginalFilename().toLowerCase()
                                
        log.debug "Creating temp file: ${fileWithPath}"
 
        File tmpFile = null
        try {
            tmpFile = new File(fileWithPath)
            cmd.tweetJsonFile.transferTo(tmpFile)
            log.debug "    done transfering, file size: ${tmpFile.length()} bytes"
            
        } catch (IllegalStateException ise) {
            log.error "IllegalStateException in saveFile, trying to save ${fileWithPath}"
            
        } catch (IOException ioe) {
            log.error "IOException in saveFile, trying to save ${fileWithPath} "
        }
        
        sw.stop()
        log.debug "Done saving file temp location at " + sw.toString()
        return [uploadedFile: tmpFile, fileWithPath: fileWithPath]
    } 
    
    /**
     *  Send email with status. 
     * 
     **/
    def sendEmail (String emailAddress, String msgSubject, String msgBody) {
        if (mailService != null) {
            executorService.submit({
                mailService.sendMail {
                    to emailAddress
                    subject msgSubject
                    body msgBody
                }
            })
        }       
        
    }
    
    /**
    * Called from -- TweetController.Upload
    * In a separate thread for processing.
    * Loads the temporary file, processes, then deletes it.
    * The file will end in .json or .tar.gz.
    * Sends an email upon starting and finishing the processing.
    * Does a few checks for a valid tweet file before processing, in case a tweet
    * ends in a NL after each element, in which case it can't be parsed.
    * Future -- remove the NLs from the file, then process. 
    * 
    **/
    def loadFile(File tempUploadFile, String emailAddress) {
        // define the message string here so the catch block has access to
        // it for reporting the error
        String str = ""
 
        try {
            log.debug "loadFile..."

            StopWatch sw = new StopWatch()
            sw.start()
            String fileName = tempUploadFile.getAbsolutePath().toLowerCase()
            log.debug "path: ${fileName}"
            InputStream fileStream = new FileInputStream(tempUploadFile)
            def theDate = new Date()
            log.debug "have a new inputstream"
          
            boolean parseError = false
            
            if (fileName.endsWith(".json")) {
                log.debug "Reading ${fileName}"
                InputStreamReader sr = new InputStreamReader(fileStream)
                // send email notifying that import is starting
                log.debug "calling sendEmail..."
                sendEmail(emailAddress, "Import Starting for ${fileName}",
                                        "Import started on " + theDate.format("EEE, d MMM yyyy HH:mm:ss"))
            
                
                str = sr.readLine()
                JsonSlurper slurper = new JsonSlurper();
                while (str != null && !parseError) {
                    def map = slurper.parseText(str)
                    // make sure there is a parsed json file and a tweet id
                    if (map.size() > 0 && map["id_str"]) {
                        loadTweet(map)
                    } else {
                        log.debug "Error parsing JSON format: ${str}"
                        parseError = true
                        sendEmail(emailAddress, "Error parsing tweet in ${fileName}", 
                                                "Error parsing tweet: ${str}")
                    } 
                    str = sr.readLine()
                }
                sr.close()
                log.debug "done reading input file!"
                
            } else if (fileName.endsWith(".tar.gz")) {
                BufferedInputStream bin = new BufferedInputStream(fileStream)
                log.debug "calling sendEmail, loading tar.gz..."
                sendEmail(emailAddress, "Import Starting for ${fileName}",
                                        "Import started on " + theDate.format("EEE, d MMM yyyy HH:mm:ss"))

                GzipCompressorInputStream gzIn = new GzipCompressorInputStream(bin)
                TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn)

                TarArchiveEntry entry = null;

                /** Read the tar entries using the getNextEntry method **/

                while ((entry = (TarArchiveEntry) tarIn.nextEntry) != null) {

                    log.debug "Extracting: ${entry.name}"

                    /** If the entry is a directory, skip **/

                    if (entry.isDirectory()) {
                    }
                    /**
                     * If the entry is a file, read each line and load the tweet
                     **/
                    else {
                        InputStreamReader sr = new InputStreamReader(tarIn)
                        str = sr.readLine()
                        JsonSlurper slurper = new JsonSlurper();
                        int count = 0;
                        // for testing, only load subset
                        //while (str != null && !parseError && count < 150) {
                        while (str != null && !parseError) {    
                            def map = slurper.parseText(str)
                            // make sure there is a parsed json file and a tweet id
                            log.debug "Checking tweet map for id_str"
                            if (map.size() > 0 && map["id_str"]) {
                                Tweet tweet = loadTweet(map)
                                str = sr.readLine()
                                count++;
                            } else {
                                log.debug "Error parsing JSON format: ${str}"
                                parseError = true
                                sendEmail(emailAddress, "Error parsing tweet in ${fileName}", 
                                                        "Error parsing tweet: ${str}")
                                
                            }
                        }
                    }
                }

                /** Close the input stream **/

                tarIn.close();
                log.debug "done reading input file!"              
                
            }
            sw.stop()
            // send a completion email only if there was no parse error
            if (!parseError) {
                sendEmail(emailAddress, "Import Complete for ${fileName}",
                                        "Import completed on " + theDate.format("EEE, d MMM yyyy HH:mm:ss") +
                                        ", ${sw.toString()}")
            }
            // delete temporary file 
            tempUploadFile.delete()
         
        } catch (FileNotFoundException fnfe) {
            log.error "File Not Found: ${fnfe.message}", fnfe
            log.debug "File Not Found: ${fnfe.message}"

            sendEmail(emailAddress, fnfe.message + ": ${fileName}",
                                    ExceptionUtils.getStackTrace(fnfe))
 
            
        } catch (Exception ex) {
            log.error "Unexpected Exception: ${ex.message}", ex
            log.debug "Unexpected Exception: ${ex.message}, tweet string: ${str}"
            sendEmail(emailAddress, ex.message + ": ${fileName}",
                                    "Tweet with the problem: ${str}, Stack trace: " + 
                                    ExceptionUtils.getStackTrace(ex))

        }                        
        
    }
    
    /**
    * Called from -- TweetController.Upload
    * **** Deprecated -- calling loadFile with a temporary upload file instead
    * 
    **/
    def loadFile(TweetCommand cmd) {
        String emailAddress = cmd.emailAddress

        try {
            StopWatch sw = new StopWatch()
            sw.start()
            String fileName = cmd.tweetJsonFile.originalFilename.toLowerCase()

            if (fileName.endsWith(".json")) {
                log.debug "Reading ${cmd.tweetJsonFile.originalFilename}"
                def is = cmd.tweetJsonFile.getInputStream()
                InputStreamReader sr = new InputStreamReader(is)

                if (mailService != null) {
                    executorService.submit({
                        mailService.sendMail {
                            to emailAddress
                            subject "Import Start"
                            body 'Import Start'
                        }
                    })
                }

                String str = sr.readLine()
                JsonSlurper slurper = new JsonSlurper();
                while (str != null) {
                    def map = slurper.parseText(str)
                    loadTweet(map)
                    str = sr.readLine()
                }

                log.debug "done reading input file!"
            } else if (fileName.endsWith(".tar.gz")) {
                def is = cmd.tweetJsonFile.getInputStream()
                
                BufferedInputStream bin = new BufferedInputStream(is)

                if (mailService != null) {
                    executorService.submit({
                        mailService.sendMail {
                            to emailAddress
                            subject "Import Start"
                            body 'Import Start'
                        }
                    })
                }
                GzipCompressorInputStream gzIn = new GzipCompressorInputStream(bin)
                TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn)

                TarArchiveEntry entry = null;

                /** Read the tar entries using the getNextEntry method **/

                while ((entry = (TarArchiveEntry) tarIn.nextEntry) != null) {

                    log.debug "Extracting: ${entry.name}"

                    /** If the entry is a directory, skip **/

                    if (entry.isDirectory()) {
                    }
                    /**
                     * If the entry is a file, read each line and load the tweet
                     **/
                    else {
                        InputStreamReader sr = new InputStreamReader(tarIn)
                        String str = sr.readLine()
                        JsonSlurper slurper = new JsonSlurper();
                        int count = 0;
                        // for testing, only load subset
                        //while (str != null && count < 200) {
                        while (str != null) {    
                            def map = slurper.parseText(str)
                            Tweet tweet = loadTweet(map)
                            str = sr.readLine()
                            count++;
                        }
                    }
                }

                /** Close the input stream **/

                tarIn.close();
                log.debug "done reading input file!"
            }
            sw.stop()
            if(mailService != null) {
                executorService.submit({
                    sendMail {
                        to emailAddress
                        subject "Import Complete"
                        body 'Import Complete: ' + sw.toString()
                    }
                })
            }
        } catch (FileNotFoundException fnfe) {
            log.error "File Not Found: ${fnfe.message}", fnfe
            executorService.submit({
                sendMail {
                    to emailAddress
                    subject fnfe.message
                    body ExceptionUtils.getStackTrace(fnfe)
                }
            })
        } catch (Exception ex) {
            log.error "Unexpected Exception: ${ex.message}", ex
            executorService.submit({
                sendMail {
                    to emailAddress
                    subject ex.message
                    body ExceptionUtils.getStackTrace(ex)
                }
            })
        }
    }
}
