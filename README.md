# dippybird

A simple clojure based blog.   Uses the filesystem to store blog entries.   The project used to use mysql, but I kept accidentally
deleting the mysql database.

## Motivation

I'm a web application developer, I can't use someone else blogging software.    A shoemaker should wear his own shoes.
A simple blogging application is pretty straightforward and I get to design and I get to craft a solution that meets
my exact needs.

## See It In Action

<a href="http://jadn.com/bob/">http://jadn.com/bob/</a>

## Blog Author

For the blog author, entries can be created by "logging in" using a special URL with a password, then the blog author
can create a blog entry including the ability to upload images.    The idea being to keep the effort required to create an entry low.

## Reusable by Others

My intention is that this application can be easily reused by others.  I intend to use it for multiple blogs myself
(one for me, and a separate blog about my kids, and some project specific blogs), so I will strive to keep the 
configuration and customization straightforward.
 
I imagine the ideal end user would be another developer who is familiar with clojure.    They might just look
at this project to see how something is done, or may customize it to suit their own specific needs.

## Prerequisites

Modify the config.edn, db.edn (which really should be parametrized), and deploy to Tomcat.

## Running

To start a web server for the application, run:

    lein ring server
    
## Features
    - image upload and handling
    - use markdown formatting
    - labels / rss (add #Bla on the title, and then access /rss/Bla as rss feed)
    
## TODO
    - better user/dev docs (need to test by asking another developer to try using)
    - document deployment (or example)
    - email subscription
    - comments
    - entry preview ?
   

## License

Distributed under the [MIT License](http://opensource.org/licenses/MIT).
Copyright © 2015,2016 Bob Herrmann

