(ns music-shmusic.server
  (:require
   [music-shmusic.config :as config]
   [music-shmusic.users :as users]

   [ring.middleware.reload :as reload]
   [ring.middleware.params :as ring-params]
   [ring.middleware.file :as file]
   [ring.middleware.keyword-params :as ring-kw-params]

   [mount.core :as m]
   [org.httpkit.server :as http-kit]))

(defn hello-handler [& [request]]
  {:status 200
   :body (str "Hello, " (or (:username users/current-user) "stranger") "!\n"
              (prn-str request))})

(def app
  (-> hello-handler
      users/ring-wrap-auth
      (file/wrap-file config/webroot-dir)
      ring-kw-params/wrap-keyword-params
      ring-params/wrap-params
      reload/wrap-reload))

(m/defstate server
  :start (http-kit/run-server #'app {:port 3000})
  :stop (server :timeout 100))
