(ns dippybird.routes.home
  (:require [dippybird.layout :as layout]
            [dippybird.db :as db ]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :refer [content-type response redirect file-response]]
            [clojure.java.io :as io]
            [ring.util.anti-forgery]
            [ring.middleware.multipart-params :as mp]
            [clojure.java.io :as io]
            [dippybird.config])
  (:import [java.io File FileInputStream FileOutputStream]))

(def human-date-formatter (java.time.format.DateTimeFormatter/ofPattern "dd-MMM-yyyy hh:mm a"))
(def dir-date-formatter (java.time.format.DateTimeFormatter/ofPattern "yyyy-MM-dd-hh:mm-a"))

(defn fix-date [entry]
  (conj entry {:fmt-date-created (:date_created entry)
               :iso-date-created "iso-date-for-rss" }))

(defn home-page [req]
  (layout/render
    "home.html" {:admin (:admin (:session req)) :entries (map fix-date (db/all-entries db/db-spec))}))

(defn rss-page [category req]
  (let [x (filter #(.contains (:title %) category) (map fix-date (db/all-entries db/db-spec)))]
    (content-type
      (layout/render
        "rss.xml" {:admin (:admin (:session req)) :iso-lastest-entry (:iso-date-created (first x)) :entries x})
      "application/atom+xml")))

(defn get-images []
  (map #(.getName %) (reverse (sort-by #(.lastModified %) (file-seq (clojure.java.io/file (:image-store-dir dippybird.config/conf)))))))

(defn edit-page [id]
  ; (if (empty? {session :session} )
  (layout/render "edit.html" {:tok (ring.util.anti-forgery/anti-forgery-field) :images (get-images) :entry (fix-date (first (db/fetch-entry db/db-spec id)))}))
  ;    (layout/render "login.html")
  ;  )

(defn new-page []
  (let [entry {:fmt-date-created (.format human-date-formatter (java.time.LocalDateTime/now))}]
    ; (if (empty? {session :session} )
    (layout/render "edit.html" {:tok (ring.util.anti-forgery/anti-forgery-field) :images (get-images) :entry entry})))
    ;    (layout/render "login.html")


(defn send-redir [where]
  (ring.util.response/redirect (str layout/*servlet-context* where)))
  ;;(ring.util.response/redirect (str where)))

(defn send-home [] (send-redir "/"))

(defn create-id-from-date [date-str]
  (let [ldt (.parse human-date-formatter date-str)]
    (.format dir-date-formatter ldt)))

(defn edit-post [id date title body]
  (if (empty? id)
    (db/insert-new-post! db/db-spec (create-id-from-date date) title body)
    (db/update-post! db/db-spec id  title body))
  (send-home))

(defn delete-page [id]
  (db/delete-post! db/db-spec id)
  (send-home))

(defn set-user! [id {session :session}]
  (-> (send-home)
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
            dest (.getChannel out)]
        (.transferFrom dest source 0 (.size source))
        (.flush out)))))

(defroutes home-routes
           (GET "/" req [] (home-page req))
           (GET "/rss/:category" [category :as req] (rss-page category req))
           (GET "/login/:password" [password :as req] (set-user! password req))
           (GET "/logout" [req] (remove-user! req))
           (GET "/new" [] (new-page))
           (GET "/edit" [id] (edit-page id))
           (POST "/edit" [id date title body] (edit-post id date title body))
           (GET "/delete" [id] (delete-page id))
           (GET "/about" [] (layout/render "about.html"))

           (POST "/upload" [file]
             (upload-file (:image-store-dir dippybird.config/conf) file)
             (send-redir "/new")))


