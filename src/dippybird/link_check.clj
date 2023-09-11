
(ns link-check
  (:require [clojure.string :as str]
            [clojure.xml :as xml]
;;            [org.ccil.cowan.tagsoup :as tagsoup]
            [clojure.java.io :as io]))

(require 'clojure.edn)
(require '[clj-time.format :as f])
(require '[clojure.string :as str])
(require '[dippybird.db :as db])

(comment
  (db/all-entries db/db-spec)

  (doseq [be (db/all-entries db/db-spec)]
    (db/update-post! db/db-spec be)))

(def tag-type "<a ")

(def human-date-formatter (java.time.format.DateTimeFormatter/ofPattern "dd-MMM-yyyy hh:mm a"))
(def dir-date-formatter (java.time.format.DateTimeFormatter/ofPattern "yyyy-MM-dd-hh:mm-a"))

(defn fs-date-to-human-date [fs-date]
  (.format human-date-formatter (.parse dir-date-formatter fs-date)))

(defn find-string-pos [acc str-to-find search-string]
  (let [idx (.lastIndexOf search-string str-to-find)]
    (if (= -1 idx)
      acc
      (find-string-pos (conj acc idx) str-to-find (subs search-string 0 idx)))))

(defn extract-href-url [tag]
  (second (re-find #"href=\s*[\"'](.*)[\"']"
                   tag)))

(def all-entries (db/all-entries db/db-spec))
(def body (:body (first all-entries)))

(defn extract-tag  [where body]
  (let [dex (inc (.indexOf body ">" where))]
    [where (subs body where dex)]))

(defn just-tags [be]
  (let [body (:body be)]
    (vector (:id be) (map #(extract-tag % body) (find-string-pos [] tag-type body)))))

(require '[clj-http.client :as client])

(defn get-url-status [url]
  (case url

    "http://rh4c.com/" "old"
    "http://groovy.codehaus.org/" "old"
    "http://geb.codehaus.org/" "old"
    "http://exchange.andblogs.net/forum/device-exchange/" "old"
    "http://exchange.andblogs.net/forum/device-exchange" "old"
    "http://listen.googlelabs.com/" "old"
    "http://twitter.com/cutshot" "old"
    "http://www.getjobdone.eu/" "old"
    "http://workingwithrails.com/" "old"
    "http://www.grailsjobs.com/" "old"
    "http://www.twhirl.org/" "blackhole"
    "http://twitter.com/bherrmann7" "old"
    "http://jhcore.com/2007/03/25/vista-on-ubuntu-using-virtualbox/" "old"
    "http://laptopgiving.org/" "old"
    "http://www.joehewitt.com/iui/" "old"
    "http://wanderingbarque.com/nonintersecting/2006/11/15/the-s-stands-for-simple/" "old"
    "http://moztraybiff.mozdev.org/" "old"
    "http://www.openqa.org/selenium-ide/" "old"
    "http://www.heliumknowledge.com/" "old"
    "http://www.sys-con.com/java/46131.cfm" "old"
    "http://www.votw.net/" "old"

    (if (.startsWith url "http")
      (try
        (let [response (client/get url  {:throw-exceptions false :socket-timeout 10000 :connection-timeout 10000})]
          (:status response) 200)
        (catch  java.net.SocketTimeoutException _ "timeout")
        (catch  java.net.ConnectException _ "connect timeout")
        (catch  java.net.UnknownHostException _ "unknown host")
        )
      "local")))

;;(get-url-status "http://www.joehewitt.com/iui/")

(def pw (java.io.PrintWriter. (java.io.FileWriter. "resources/public/review-links.html")))

(defn prt [& strngs]
  (.println pw (apply str strngs)))

#_(defn tag-status [tag]
    (let [url-start-raw (.indexOf tag url-attribute)]
      (if (= url-start-raw -1)
        (str "MISSING " url-attribute " PART")
        (let [url-start (+ (count url-attribute) url-start-raw)
              url-end (.indexOf tag "\"" url-start)]
          (subs tag url-start url-end)))))

"<a href= \"https://play.google.com/store/apps/details?id=com.jadn.com.tip_calculator&amp;pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1\">"

(defn dump-url-statuses [be]
  (println "Processing " (.indexOf all-entries be) (:id be))
  (let [tags-seq (second (just-tags be))]
    (if (> (count tags-seq) 0)
      (do
        (prt (:date_created be) " cnt=" (count tags-seq) "<blockquote>")
        (doall (for [item tags-seq]
                 (let [link-url  (extract-href-url (second item))
                       _ (println "Trying -->" link-url "<<-- from " (second item))
                       url-status (get-url-status link-url)]
                   (prt  "<br>" url-status " " (extract-href-url (second item))))))
        (prt "</blockquote>"))
      nil)))

;; (def elinks (second (just-tags (get all-entries 3))))

(doseq [be all-entries] (dump-url-statuses be))

(.close pw)



