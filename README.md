# Social Media Scraper

This Grails application imports Tweet data from a .tar.gz file or a .json file.
Data can be viewed with the built-in views or accessed via REST.

This application will take in a Tweet dataset and resolve all shortened URLs and create a linked network of tweets to retweets and users.

## Database Support

Development is usually done using h2 for speed, but production is based on MySQL.  However, tweet data often has UTF-8 characters that are beyond the native support of MySQL.  Included are 2 different configurations to get MySQL to support the extra characters.  This work is based on this [article](https://mathiasbynens.be/notes/mysql-utf8mb4).

### Windows

The [my.ini](my.ini) file belongs in `c:\ProgramData\MySQL\MySQL Server 5.7\`.  Note, this is not the `Program Files` directory.  It is hidden from view and must be accessed specifically.

### Linux (Ubuntu)

The [mysql.cnf](mysql.cnf) file belongs in `/etc/mysql/mysql.conf.d` directory.  This assumes that the `/etc/mysql/my.cnf` files contains the following line: `!includedir /etc/mysql/mysql.conf.d/`

### Creating the Database

```sql
CREATE DATABASE smsProd DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

## REST

Current support is provided through the `RestLinkController` and `RestTweetController`.

## Known Issues

 - MySQL support noted above
 - Time consuming.  Adding in an 'e-mail when complete' feature may be necessary

## Future

 - Further or better support for REST
 - Domain expansion for future data (credibility of links, etc.)
