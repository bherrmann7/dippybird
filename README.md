# dippybird

A simple clojure based blog.   Uses mysql for storing entries (overkill?)

## Motivation

I'm a web application developer, I can't use someone else blogging software.    A shoemaker should wear his own shoes.
A simple blogging application is pretty straightforward and I get to design and I get to craft a solution that meets
my exact needs.

## See It In Action

<a href="http://jadn.com/bob">http://jadn.com/bob</a>

## Reusable by Others

My intention is that this application can be eaily reused by others.  I intend to use it for multiple blogs myself
(one for me, and a separate blog about my kids, and some project specific blogs), so I will strive to keep the 
configuration and customization straightforward.
 
I image the ideal end user would be another hacker who is familure with mysql and clojure.    They might just look
at this project to see how something is done, or may customize it to suit their own specific needs.

## Prerequisites

set up mysql

    mysql> create database someblog
    mysql> grant all PRIVILEGES on someblog.* to 'someuser'@'localhost' identified by 'YOUR_PASS';
    mysql> @schema.sql


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

