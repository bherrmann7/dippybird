(ns dippybird.config)

; Something to hold all the configuration options

; would be nice if this hooked into the application init, and used the application name as part of the key
; for looking up this information in a "well known place" - the idea being a server might host multiple
; blogs served by different copies of dippybird
; or perhaps the inbound hosts name

(def conf
  {; path where images are served from
   :image-serve-root "/home/bob/wpub/"

   ; actual location of files (historically, I've always used res/aimg/... for images in blogs
   :image-store-dir "/home/bob/wpub/res/aimg/"

   ; use this to login via a url like so, /login/PASSWORD   Putting the login in a URL makes it easy to
   ; just use a bookmark as a way to login
   :admin-pass (slurp "/home/bob/bin/bobblog.pass")})