# dippybird

A simple clojure based blog.   Uses mysql for storing entries (overkill?)

## See it in action

    http://jadn.com/bob

## Prerequisites

set up mysql

mysql> create bob_blog
mysql> mysql> grant all PRIVILEGES on bob_blog.* to 'bobblog'@'localhost' identified by 'YOUR_PASS';


## Running

To start a web server for the application, run:

    lein ring server
    
## TODO
    
    - image upload and handling
    - labels / rss
    - email subscription
    - comments
    - use markdown for entries
    - entry preview

## License

Copyright © 2015 Bob Herrmann

