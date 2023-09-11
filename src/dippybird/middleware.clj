(ns dippybird.middleware
  (:require [dippybird.session :as session]
            [dippybird.config]
            [dippybird.layout :refer [*servlet-context*]]
  ;;          [taoensso.timbre :as timbre]
;;            [environ.core :refer [env]]
            [selmer.middleware :refer [wrap-error-page]]
;;            [prone.middleware :refer [wrap-exceptions]]
            [ring.util.response :refer [redirect]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.session-timeout :refer [wrap-idle-session-timeout]]
            [ring.middleware.session.memory :refer [memory-store]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.middleware.file]))

(defn wrap-servlet-context [handler]
  (fn [request]
    (binding [*servlet-context*
              (if-let [context (:servlet-context request)]
                ;; If we're not inside a serlvet environment
                ;; (for example when using mock requests), then
                ;; .getContextPath might not exist
                (try (.getContextPath context)
                     (catch IllegalArgumentException _ context)))]
      (handler request))))

(defn wrap-internal-error [handler]
  (fn [req]
    (try
      (handler req)
      (catch Throwable t
        ;;(timbre/error t)
        (.printStackTrace t)
        {:status 500
         :headers {"Content-Type" "text/html"}
         :body (str "<body>
                  <h1>An exception occurred...</h1>
                  <p>Thread message: " (.getMessage t)
                 "<pre>" (pr-str t) "</pre>"
                "</body>")}))))

(defn development-middleware [handler]
  (if true #_(env :dev)
    (-> handler
        wrap-error-page
        #_wrap-exceptions)
    handler))


(defn production-middleware [handler]
  (-> handler
      (wrap-restful-format :formats [:json-kw :edn :transit-json :transit-msgpack])
      (wrap-idle-session-timeout
       {:timeout (* 60 30)
        :timeout-response (redirect "/")})
      (ring.middleware.file/wrap-file (:image-serve-root dippybird.config/conf))
      (wrap-defaults
       (assoc-in site-defaults [:session :store] (memory-store session/mem)))
      wrap-servlet-context
      wrap-internal-error))

