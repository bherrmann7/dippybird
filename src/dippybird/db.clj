(ns dippybird.db)
(require 'clojure.edn)
(require '[clj-time.format :as f])
(require '[clojure.string :as str])

;; Not currently used (used to use mysql, and this parameter used to specify how to connect to mysql), could hold base directory
(def db-spec "cow") 

(def blog-base-dir (java.io.File. "/home/bob/dippy.dir/data/"))

(defn blog-entry-dirs [] (reverse (sort (map str (flatten (map #(seq (.listFiles %)) (seq (.listFiles blog-base-dir))))))))

(def date-formatter (java.text.SimpleDateFormat. "yyyy-MM-dd-hh:mm-aa"))

(defn blog-entry-as-map [ dir ]
  (let [
        body (slurp (str dir "/body.html"))
        ;;body (extract-div-wrapper (body-remove-extra-div dir body-raw))
        ;;body (img-transform body-raw-img (reverse (image-starts [] body-raw-img 0)))
        data (clojure.edn/read-string (slurp (str dir "/data.edn")))
        title (clojure.string/replace (:title data) "&nbsp;" "")
        fs-date (subs dir (inc (.lastIndexOf dir "/")))
        date (.parse date-formatter fs-date)
        ]
     {:id fs-date :title title :date_created date :body body}))

(defn all-entries [ db-spec ]
  (into [] (map blog-entry-as-map (blog-entry-dirs))))

;; /home/bob/prj/sippy-chicken/data/2021-01-23-12:08-AM/body.html (No such file or directory)
(defn make-file-path-from-id [id]
  (let [ year (subs id 0 4) ]
    (str blog-base-dir "/" year "/" id )))

(defn fetch-entry [db-spec id]
  [(blog-entry-as-map (make-file-path-from-id id))])

(defn update-post!
  ([db-spec be ]
   (update-post! db-spec (:id be) (:title be) (:body be)))
  ([db-spec id title body ]
   (let [dir-path (make-file-path-from-id id)
         data-file (str dir-path "/data.edn")
         body-file (str dir-path "/body.html")
         ]
     (spit data-file (pr-str {:title title }))
     (spit body-file body)
     )))

(defn delete-post! [db-spec id]
  (println "I should delete post" id)
  (let [dir-path (java.io.File. (make-file-path-from-id id))]
    (if (not (.exists dir-path))
      (throw (RuntimeException. (str "Unable to locate post using id=" id))))
    (println "I should delete dir" dir-path)
    (doall (map #(do
            (println "--> Delete" %)
            (.delete %)
            ) (seq (.listFiles dir-path))))
    (.delete dir-path)
    ))

(defn insert-new-post! [db-spec id title body]
  (let [dir-path (java.io.File. (make-file-path-from-id id))
         data-file (str dir-path "/data.edn")
        body-file (str dir-path "/body.html")
        ]
    ;; make year if needed
    (.mkdir (.getParentFile dir-path))
    (if (.exists dir-path) (throw (RuntimeException. "Already have blog entry for that date/time!")))
    (.mkdir dir-path)
    (spit data-file (pr-str {:title title }))
    (spit body-file body)))
    
(comment
  (def a (all-entries db-spec))

  (defn remove-archive-body [ be ]
    (assoc be :body (str/replace (:body be) "https://web.archive.org/web/20211026170618/" "")))

  (def fixed (map remove-archive-body a))

  (map #(update-post! "cow" %) fixed)
  )


  
