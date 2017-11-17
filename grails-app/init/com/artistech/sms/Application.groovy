package com.artistech.sms

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.boot.Banner
import grails.util.Environment
import static grails.util.Metadata.current as metaInfo

class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        final GrailsApp app = new GrailsApp(Application)
        app.banner = new GrailsBanner()
        app.run(args)
    }
}

/**
 * Class that implements Spring Boot Banner
 * interface to show information on application startup.
 */
class GrailsBanner implements Banner {

    //http://patorjk.com/software/taag/#p=display&f=Graffiti&t=OntoFlex
    private static final String BANNER = '''
  _________                    .__.__      _____             .___.__         _________                                        
 /   _____/ ____   ____ _____  |__|  |    /     \\   ____   __| _/|__|____   /   _____/ ________________  ______   ___________ 
 \\_____  \\ /  _ \\_/ ___\\\\__  \\ |  |  |   /  \\ /  \\_/ __ \\ / __ | |  \\__  \\  \\_____  \\_/ ___\\_  __ \\__  \\ \\____ \\_/ __ \\_  __ \\
 /        (  <_> )  \\___ / __ \\|  |  |__/    Y    \\  ___// /_/ | |  |/ __ \\_/        \\  \\___|  | \\// __ \\|  |_> >  ___/|  | \\/
/_______  /\\____/ \\___  >____  /__|____/\\____|__  /\\___  >____ | |__(____  /_______  /\\___  >__|  (____  /   __/ \\___  >__|   
        \\/            \\/     \\/                 \\/     \\/     \\/         \\/        \\/     \\/           \\/|__|        \\/       

'''

    @Override
    void printBanner(
            org.springframework.core.env.Environment environment,
            Class<?> sourceClass,
            PrintStream out) {
        out.println BANNER

        row 'App version', metaInfo.getApplicationVersion(), out
        row 'App name', metaInfo.getApplicationName(), out
        row 'Grails version', metaInfo.getGrailsVersion(), out
        row 'Groovy version', GroovySystem.version, out
        row 'JVM version', System.getProperty('java.version'), out
        row 'Reloading active', Environment.reloadingAgentEnabled, out
        row 'Environment', Environment.current.name, out

        out.println()
    }

    private void row(final String description, final value, final PrintStream out) {
        out.print ':: '
        out.print description.padRight(16)
        out.print ' :: '
        out.println value
    }

}