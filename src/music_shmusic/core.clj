(ns music-shmusic.core
  (:require
   [compojure.core :as cc]
   [compojure.handler :as handler]
   [compojure.route :as route]
   [alex-and-georges.debug-repl :as debug]
   ;; [com.gfredericks.debug-repl :as debug]
   [music-shmusic.users :as users]
   [ring.middleware.params :as ring-params]
   [ring.middleware.keyword-params :as ring-kw-params]))

(defn username-or-stranger []
  (if-let [{:keys [username]} users/current-user]
    username
    "stranger"))

(defn hello-handler [& [request]]
  {:status 200
   :body (str "Hello, " (username-or-stranger) "!\n"
              (prn-str request))})

(defn wrap-auth [next-handler]
  (fn [{:keys [params] :as request}]
    (if-let [user (users/authenticate-user params)]
      (binding [users/current-user user]
         (next-handler request))
       (next-handler request))))

(cc/defroutes app-routes
  (cc/GET "/"
          request
          (hello-handler request))
  (route/resources "")
  (route/not-found "<p>Page not found.</p>"))

(def app
  (-> (handler/site app-routes)
      wrap-auth
      ring-kw-params/wrap-keyword-params
      ring-params/wrap-params))










