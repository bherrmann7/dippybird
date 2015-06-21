(ns dippybird.routes.home
  (:require [dippybird.layout :as layout]
            dippybird.db
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :refer [response redirect file-response]]
            [clojure.java.io :as io]
            [ring.util.anti-forgery]
            [clj-time.format :as f]
            [clj-time.coerce]
            [ring.middleware.multipart-params :as mp]
            [clojure.java.io :as io]
            [dippybird.config])
  (:import [java.io File FileInputStream FileOutputStream]))

(def custom-formatter (f/formatter "dd-MMM-yyyy hh:mm aa"))

(defn fix-date [entry]
  (conj entry {:date_created (f/unparse custom-formatter (clj-time.coerce/from-long (.getTime (:date_created entry))))}))

(defn home-page [req]
  (layout/render
   "home.html" {:admin (:admin (:session req)) :entries (map fix-date (dippybird.db/all-entries dippybird.db/db-spec))}))

(defn get-images []
  (map #(.getName %) (reverse (sort-by #(.lastModified %) (file-seq (clojure.java.io/file (:image-store-dir dippybird.config/conf)))))))

(defn edit-page [id]
  ; (if (empty? {session :session} )
  (layout/render "edit.html" {:tok (ring.util.anti-forgery/anti-forgery-field) :images (get-images) :entry (fix-date (first (dippybird.db/fetch-entry dippybird.db/db-spec id)))})
  ;    (layout/render "login.html")
  ;  )
  )


(defn new-page []
  (let [entry {:date_created (f/unparse custom-formatter nil)}]
    ; (if (empty? {session :session} )
    (layout/render "edit.html" {:tok (ring.util.anti-forgery/anti-forgery-field) :images (get-images)  :entry entry})
    ;    (layout/render "login.html")
    ;  )
    ))


(defn send-redir [where]
  (ring.util.response/redirect (str layout/*servlet-context* where)))

(defn send-home [] (send-redir "/"))

(defn edit-post [id date title body]
  (println "boo" id date title body)
  (if (empty? id)
    (dippybird.db/insert-new-post! dippybird.db/db-spec body (.toDate (f/parse custom-formatter date)) title)
    (dippybird.db/update-post! dippybird.db/db-spec (.toDate (f/parse custom-formatter date)) title body id))

  (send-home))

(defn delete-page [id]
  (dippybird.db/delete-post! dippybird.db/db-spec id)
  (send-home))

(defn set-user! [id {session :session}]
  (->    (send-home)
         (assoc :session (assoc session :admin (= (:admin-pass dippybird.config/conf) id)))))

(defn remove-user! [{session :session}]
  (-> (response "")
      (assoc :session (dissoc session :user))))


; http://www.luminusweb.net/docs/routes.md
(defn file-path [path & [filename]]
  (java.net.URLDecoder/decode
   (str path File/separator filename)
   "utf-8"))

(defn upload-file
  "uploads a file to the target folder
   when :create-path? flag is set to true then the target path will be created"
  [path {:keys [tempfile size filename]}]
  (try
    (with-open [in (new FileInputStream tempfile)
                out (new FileOutputStream (file-path path filename))]
      (let [source (.getChannel in)
            dest   (.getChannel out)]
        (.transferFrom dest source 0 (.size source))
        (.flush out)))))

(defroutes home-routes
  (GET "/" req [] (home-page req))
  (GET "/login/:password" [password :as req] (set-user! password req))
  (GET "/logout" [req] (remove-user! req))
  (GET "/new" [] (new-page))
  (GET "/edit" [id] (edit-page id))
  (POST "/edit" [id date title body] (edit-post id date title body))
  (GET "/links" [] (layout/render "links.html"))
  (GET "/delete" [id] (delete-page id))
  (GET "/about" [] (layout/render "about.html"))

  (POST "/upload" [file]
    (upload-file (:image-store-dir dippybird.config/conf) file)
    (send-redir "/new")))


