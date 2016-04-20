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
 
I image the ideal end user would be another hacker who is familiar with mysql and clojure.    They might just look
at this project to see how something is done, or may customize it to suit their own specific needs.

## Prerequisites

set up mysql

    mysql> create database someblog
    mysql> grant all PRIVILEGES on someblog.* to 'someuser'@'localhost' identified by 'YOUR_PASS';
    mysql> @schema.sql


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

