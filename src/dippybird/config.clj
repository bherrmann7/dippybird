
(ns dippybird.config)

; Something to hold all the configuration options

; would be nice if this hooked into the application init, and used the application name as part of the key
; for looking up this information in a "well known place" - the idea being a server might host multiple
; blogs served by different copies of dippybird
; or perhaps the inbound hosts name

;; Seems odd that these config options are not loaded dynamically based on the context.
;; ie $USER.HOME/dippy-config-CONTEXT.edn
;;

(def conf
  {
   ;; note that db.clj also has a hard coded path
   
   ;; path where images are resources are from (ring looks here for files)
   :image-serve-root "/home/bob/dippy.dir/www/"

   ;; actual location of files (historically, I've always used res/aimg/... for images in blogs
   ;; keeps uploaded blog images away from resource images (so they dont collide)
   :image-store-dir "/home/bob/dippy.dir/www/imgs/"

   ; use this to login via a url like so, /login/PASSWORD   Putting the login in a URL makes it easy to
   ; just use a bookmark as a way to login
   :admin-pass (slurp "/home/bob/bin/dippyblog.pass")})



