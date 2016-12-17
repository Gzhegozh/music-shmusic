(ns music-shmusic.server
  (:require
   [music-shmusic.config :as config]
   [music-shmusic.users :as users]
   [hiccup.page :refer [include-js include-css html5]]
   [ring.adapter.jetty :refer [run-jetty]]

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

(def sign-in-content
    [:div#sign-in-content])

(def sign-up-content
    [:div#sign-up-content])

(defn head []
    [:head
     [:link {:rel "stylesheet" 
            :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" 
            :integrity "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" 
            :crossorigin "anonymous"}]
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]])

(defn sign-in-handler [& [request]]
  (response/response (html5
    (head)
    [:body
      [:div {:class "container"} 
        sign-in-content]]    
     (include-js "target/app.js"))))

(defn sign-up-handler [& [request]]
  (response/response (html5  
    (head)
    [:body   
      [:div {:class "container"} 
        sign-up-content]]
        (include-js "target/app.js"))))

(defn not-found [& [request]]
  (response/not-found "Sorry, 404"))
 
(def routes ["/" {"index" hello-handler
                  "sign_in" sign-in-handler
                  "sign_up" sign-up-handler
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
