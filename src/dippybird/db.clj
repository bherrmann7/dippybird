(ns dippybird.db)

(require '[yesql.core :refer [defqueries]])

(def db-spec (read-string (slurp (str (System/getProperty "user.home") "/bin/dippybird-db.edn"))))

(comment typical db dippybird-db.edn
         {; :classname "org.postgresql.Driver"
          :subprotocol "mysql"
          :subname     "//localhost/bob_blog"
          :user        "bobblog"
          :password    "bla"})

(defqueries "sql/dippy.sql")

; (println (all-entries db-spec))