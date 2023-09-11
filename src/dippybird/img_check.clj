
;; I used this code to build a local page "review-images.html" which I used to visually inspect that
;; every image tag was showing an image.   If the image was missing, I would go hunt it down.

(ns img-check
  (:require [clojure.string :as str]
            [clojure.xml :as xml]
;;            [org.ccil.cowan.tagsoup :as tagsoup]
            [clojure.java.io :as io]))

(require 'clojure.edn)
(require '[clj-time.format :as f])
(require '[clojure.string :as str])
(require '[dippybird.db :as db ])

(comment
  (db/all-entries db/db-spec)

  (doseq [be (db/all-entries db/db-spec)]
    (db/update-post! db/db-spec be))
)

(def human-date-formatter (java.time.format.DateTimeFormatter/ofPattern "dd-MMM-yyyy hh:mm a"))
(def dir-date-formatter (java.time.format.DateTimeFormatter/ofPattern "yyyy-MM-dd-hh:mm-a"))

(defn fs-date-to-human-date [ fs-date ]
  (.format human-date-formatter (.parse dir-date-formatter fs-date)))

(defn find-string-pos [acc str-to-find search-string]
  (let [idx (.lastIndexOf search-string str-to-find)]
    (println idx)
    (if (= -1 idx)
      acc
      (find-string-pos (conj acc idx) str-to-find (subs search-string 0 idx)))))


(def a (db/all-entries db/db-spec))
(def body (:body (first a)))

(defn extract-tag  [where body]
  (let [ dex (inc (.indexOf body ">" where )) ]
    [ where (subs body where dex) ]))

(defn just-tags [ be ]
  (let [body (:body be)]
    (vector (:id be) (map #(extract-tag % body) (find-string-pos [] "<img " body)))))


(require '[clj-http.client :as client])

(defn url-status [url]
  (let [response (client/get url)]
    (:status response) 200))


;; (def pw (java.io.PrintWriter. (java.io.FileWriter. "resources/public/review-images.html")))

;; (defn prt [ & strngs ]
;;   (.println pw (apply str strngs)))

;; (defn img-status [ img-tag ]
;;   (let [ url-start (+ 5 (.indexOf img-tag "src=\""))
;;         url-end (.indexOf img-tag "\"" url-start)]
;;     (subs img-tag url-start url-end)))

;; (defn dump-url-statuses [be]
;;   (let [ tags-seq (second (just-tags be))]
;;     (prt (:date_created be) "<br><blockquote>")
;;     (doall (for [item tags-seq]
;;       (prt "<hr>" (str/replace (second item) "<" "&lt; ") (img-status (second item) ) " " (second item) )))
;;     (prt "</blockquote>")
;;     ))

;; (doseq [ be a] (dump-url-statuses be) )

;; (.close pw)



