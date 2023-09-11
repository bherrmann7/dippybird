(defproject dippybird "0.0.1-SNAPSHOT"

  :dependencies [
                 [org.clojure/clojure "1.7.0"]
                 [selmer "0.8.2"]
                 [clj-tagsoup "0.3.0"  :exclusions [org.clojure/clojure] ]
;;                 [org.ccil.cowan.tagsoup/tagsoup "1.2.1"]
;;                 [org.clojure/tools.nrepl "0.2.10"]
;;                 [com.taoensso/timbre "3.4.0"]
;;                 [com.taoensso/tower "3.0.2"]
;;                 [markdown-clj "0.9.66"]
;;                 [environ "1.0.0"]
;;                 [im.chit/cronj "1.4.3"]
                 [compojure "1.3.3"]
                 [ring/ring-defaults "0.1.4"]
                 [ring/ring-session-timeout "0.1.0"]
                 [ring-middleware-format "0.5.0"]
                 [clj-http "3.12.3"]
                 
;;                 [org.clojure/http-kit "2.4.0"]
;;                 [bouncer "0.3.2"]
;;                 [prone "0.8.1"]
                 [ring-server "0.4.0"]
;;                 [yesql "0.4.0"]
;;                 [mysql/mysql-connector-java "5.1.32"]
                 [clj-time "0.9.0"]]


  :min-lein-version "2.0.0"
  :uberjar-name "dippybird.jar"
  :jvm-opts ["-server"]

;;enable to start the nREPL server when the application launches
;:env {:repl-port 7001}

  :main dippybird.core

  :plugins [[lein-ring "0.9.1"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]
            [lein-cljfmt "0.1.10"]]


  :ring {:handler dippybird.handler/app
         :init    dippybird.handler/init
         :destroy dippybird.handler/destroy
         :uberwar-name "dippybird.war"}

  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
             
             :aot :all}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]
                        [pjstadig/humane-test-output "0.7.0"]]

         :source-paths ["env/dev/clj"]
         
         :repl-options {:init-ns dippybird.core}
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}})
