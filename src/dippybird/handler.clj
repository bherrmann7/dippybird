(ns dippybird.handler
  (:require [compojure.core :refer [defroutes routes]]
            [dippybird.routes.home :refer [home-routes]]

            [dippybird.middleware
             :refer [development-middleware production-middleware]]
            [dippybird.session :as session]
            [compojure.route :as route]
            [selmer.parser :as parser]
            )
  )

(defroutes base-routes
           (route/resources "/")
           (route/not-found "Not Found"))

#_(defn start-nrepl
    "Start a network repl for debugging when the :repl-port is set in the environment."
    []
    #_(when-let [port (env :repl-port)]
        (try
          (reset! nrepl-server (nrepl/start-server :port port))
          (pr "nREPL server started on port" port)
          (catch Throwable t
            (pr "failed to start nREPL" t)))))

#_(defn stop-nrepl []
    (when-let [server @nrepl-server]
      (nrepl/stop-server server)))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (pr "dippybird init.")
  )

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (pr "dippybird shutdown complete!")
  (pr "shutdown complete!"))

(def app
  (-> (routes
        home-routes
        base-routes)
      development-middleware
      production-middleware))
