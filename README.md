# Social Media Scraper

This Grails application imports Tweet data from a .tar.gz file or a .json file.
Data can be viewed with the built-in views or accessed via REST.

This application will take in a Tweet dataset and resolve all shortened URLs
and create a linked network of tweets to retweets and users.

## Environment Setup

The following environment variables must be set:

JAVA_HOME - be sure this points to your JDK directory, not the JRE directory.
GRAILS_HOME - path to where grails was unzipped

Append to your PATH variable:

(windows)
%PATH%;%JAVA_HOME%\bin;%GRAILS_HOME%\bin

(linux)
${PATH}:${JAVA_HOME}:${GRAILS_HOME}

## Configuration Setup

Grails is configured primarily through grails-app/conf/application.yml.

The mysql username and password are stored here. By default the username is
'sms' and the password is also 'sms'. Other mysql options in this file include
the URL of the mysql server, which should be set appropriately.

## Database Support

Development is usually done using h2 for speed, but production is based on
MySQL.  However, tweet data often has UTF-8 characters that are beyond the
native support of MySQL.  Included are 2 different configurations to get MySQL
to support the extra characters.  This work is based on this
[article](https://mathiasbynens.be/notes/mysql-utf8mb4).

### Windows

The [my.ini](my.ini) file belongs in `c:\ProgramData\MySQL\MySQL Server 5.7\`.
Note, this is not the `Program Files` directory.  It is hidden from view and
must be accessed specifically.

### Linux (Ubuntu)

The [mysqld.cnf](mysqld.cnf) file belongs in `/etc/mysql/mysql.conf.d`
directory.  This assumes that the `/etc/mysql/my.cnf` files contains the
following line: `!includedir /etc/mysql/mysql.conf.d/`

### Creating the Database

Install MYSQL using AMPPS (Windows), MAMP (OSX), or LAMP (linux). The following
commands assume a root user with no password. To add a password, add
'-p<password>' to the command.

mysql -u root -e "CREATE USER 'sms'@'localhost' IDENTIFIED BY 'sms';"
mysql -u root -e "CREATE DATABASE smsProd DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
mysql -u root -e "GRANT ALL ON smsProd.* TO 'sms'@'localhost';"

## REST

Current support is provided through the `RestLinkController` and `RestTweetController`.

## Known Issues

 - MySQL support noted above
 - Time consuming.  An 'e-mail when complete' feature has been added, supporting gmail addresses, 
                    but needs further testing.
 - Upload error occasionally, resulting in the temporary file not being found. A re-write of this is in progress.

## Future

 - Further or better support for REST
 - Domain expansion for future data (credibility of links, etc.)
 - Hashtag extraction
