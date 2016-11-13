(ns music-shmusic.core
  (:require
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

(def app
  (-> hello-handler
      users/ring-wrap-auth
      ring-kw-params/wrap-keyword-params
      ring-params/wrap-params))
