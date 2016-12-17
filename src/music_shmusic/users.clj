(ns music-shmusic.users
  (:require [gravatar.core :as gr])
  (:require [music-shmusic.config :as config]))

(use '[clojure-vk.core :as vk-core], '[clojure-vk.auth :as vk-auth])

(def users (atom []))
(def ^:dynamic current-user nil)

(defn all-users [] @users)

(defn create-user [username password]
  (swap! users conj {:username username :password password}))

(defn find-user [username]
  (->> (all-users)
       (filter #(= username (:username %)))
       first))

(comment
  (all-users)
  (find-user "vasya")
  (authenticate-user {:username "vasya", :password "123"})
  )

(create-user "vasya" "123")

(defn validate-password [user password]
  (= (:password user) password))

(defn authenticate-user [{:keys [username password]}]
  (if-let [user (find-user username)]
    (if (validate-password user password)
      user)
    false))

(defn ring-wrap-auth [next-handler]
  (fn [{:keys [params] :as request}]
    (if-let [user (authenticate-user params)]
      (binding [current-user user]
        (next-handler request))
      (next-handler request))))

(defn get-gravatar-url [email]
  (gr/avatar-url email))

(defn vk-authorize []
  (vk-auth/get-auth-url
    config/vk-app-id
    "http://localhost:3000/index"
    :display-page
    {:scope (vk-auth/scope :offline),
     :response-type "token"}))
