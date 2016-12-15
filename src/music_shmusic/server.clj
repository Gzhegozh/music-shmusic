(ns music-shmusic.server
  (:require
   [music-shmusic.config :as config]
   [music-shmusic.users :as users]

   [ring.middleware.reload :as reload]
   [ring.middleware.params :as ring-params]
   [ring.middleware.file :as file]
   [ring.middleware.keyword-params :as ring-kw-params]
   [ring.util.response :as response]

   [datomic.api :as d]
   
   [mount.core :as m]
   [bidi.ring :refer (make-handler)]
   [org.httpkit.server :as http-kit]))

;; ===== db connection =====
(defonce conn (atom nil))

(defn init-conn []
  (reset! conn (d/connect config/db-uri)))


;; ===== requests handlers =====
(defn hello-handler [& [request]]
  (response/response (str "Hello, "
                          (or (:username users/current-user)
                              "stranger")
                          "!\n"
                          (prn-str request))))

(defn login-handler [& [request]]
  (response/response "Login page"))

(defn register-handler [& [request]]
  (response/response "Register page"))

(defn not-found [& [request]]
  (response/not-found "Sorry, 404"))
 
(def routes ["/" {"index" hello-handler
                  "login" login-handler
                  "register" register-handler
                  true not-found}])

(def handler
  (make-handler routes))

(def app
  (-> handler
      users/ring-wrap-auth
      (file/wrap-file config/webroot-dir)
      ring-kw-params/wrap-keyword-params
      ring-params/wrap-params
      reload/wrap-reload))

(m/defstate server
  :start (http-kit/run-server #'app {:port 3000})
  :stop (server :timeout 100))
