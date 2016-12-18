(ns music-shmusic.db.api
  (:require [datomic.api :as d]
            [music-shmusic.config :as config]))

;; ===== db connection =====
(defonce conn (atom nil))

(defn init-conn []
  (reset! conn (d/connect config/db-uri)))

(defn db []
  (d/db @conn))

;; ===== db access =====

(def query #(apply d/q %1 (db) %&))

(def command #(d/transact @conn %))

(defn existed-entity [id]
  (ffirst (query '[:find ?eid :in $ ?eid :where [?eid]] id)))

(defn entity [eid]
  (if-let [exists (existed-entity eid)]
    (d/entity (db) exists)
    nil))

(defn entity-attrs [eid]
  (if-let [exists (existed-entity eid)]
    (d/pull (db) '[*] exists)
    nil))

(defn retract-entity [eid]
  (when-let [exists (existed-entity eid)]
    (command [[:db.fn/retractEntity exists]])))

(defn update-entity [eid data]
  (when-let [exists (existed-entity eid)]
    (command [(assoc data :db/id exists)])))

(defn create-entities [data]
  (command data))
