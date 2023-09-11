
(ns dippybird.core
  (:require [dippybird.handler :refer [app init destroy]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :as reload])
  (:gen-class))

(defonce server (atom nil))

(defn parse-port [port]
  (Integer/parseInt (or port "3000")))

(defn start-server [port]
  (init)
  (reset! server
          (run-jetty
           #_(if (env :dev) (reload/wrap-reload #'app) app)
           app
           {:port port
            :join? false})))

(defn stop-server []
  (when @server
    (destroy)
    (.stop @server)
    (reset! server nil)))

(defn -main [& [port]]
  (let [port (parse-port port)]
    (.addShutdownHook (Runtime/getRuntime) (Thread. stop-server))
    (start-server port)))


;; Presumably this file is only used during development, as the live website is hosted using tomcat via a WAR file
;; (-main)
