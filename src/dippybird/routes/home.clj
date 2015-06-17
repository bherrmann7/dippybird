(ns dippybird.routes.home
  (:require [dippybird.layout :as layout]
            dippybird.db
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :refer [response]]
            [clojure.java.io :as io]
            [ring.util.anti-forgery]
            [clj-time.format :as f]
            [clj-time.coerce]
            ))

(def custom-formatter (f/formatter "dd-MMM-yyyy hh:mm aa"))

(defn fix-date [entry]
  (conj entry {:date_created (f/unparse custom-formatter (clj-time.coerce/from-long (.getTime (:date_created entry))))})
  )

(defn home-page [req]
  (layout/render
    "home.html" {:admin (:admin (:session req)) :entries (map fix-date (dippybird.db/all-entries dippybird.db/db-spec ))}))


(defn edit-page [id]
  (println "fix date" (fix-date (first  (dippybird.db/fetch-entry dippybird.db/db-spec id))) )
 ; (if (empty? {session :session} )
    (layout/render "edit.html" {:tok (ring.util.anti-forgery/anti-forgery-field) :entry (fix-date (first  (dippybird.db/fetch-entry dippybird.db/db-spec id))) })
;    (layout/render "login.html")
;  )
)

(defn new-page [id]
  (let [entry {:date_created (f/unparse custom-formatter nil)}]
    ; (if (empty? {session :session} )
    (layout/render "edit.html" {:tok (ring.util.anti-forgery/anti-forgery-field) :entry entry})
    ;    (layout/render "login.html")
    ;  )
    )
  )


(defn send-redir [where]
  (ring.util.response/redirect (str layout/*servlet-context* where))
  )

(defn send-home [] (send-redir "/"))

(defn edit-post [id date title body  ]
  (println "all is " id date title body)
  (println "date created is " (f/parse custom-formatter date) title body)

  (if (empty? id)
    (dippybird.db/insert-new-post! dippybird.db/db-spec body (.toDate (f/parse custom-formatter date)) title  )
    (dippybird.db/update-post! dippybird.db/db-spec (.toDate (f/parse custom-formatter date)) title body id) )

  (send-home)
  )

(defn delete-page [id]
  (dippybird.db/delete-post! dippybird.db/db-spec id  )
  (send-home)
  )

(defn set-user! [id {session :session}]
  (->    (send-home)
         (assoc :session (assoc session :admin (= (slurp "/home/bob/bin/bobblog.pass") id)))))

(defn remove-user! [{session :session}]
  (-> (response "")
      (assoc :session (dissoc session :user))))


(defroutes home-routes
           (GET "/" req [] (home-page req))
           (GET "/login/:password" [password :as req] (set-user! password req))
           (GET "/logout" [req] (remove-user! req))
           (GET "/new" [id] (new-page id))
           (GET "/edit" [id] (edit-page id))
           (POST "/edit" [id date title body] (edit-post id date title body))
           (GET "/links" [] (layout/render "links.html"))
           (GET "/delete" [id] (delete-page id))
           (GET "/about" [] (layout/render "about.html"))
           )
