(ns music-shmusic.users)

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
