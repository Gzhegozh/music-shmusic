(ns music-shmusic.db.api)

(require '[datomic.api :as d]
         '[music-shmusic.server :as server])


(defn db []
  (d/db @server/conn))

(def query #(apply d/q %1 (db) %&))

(def command #(d/transact @server/conn %))

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

(defn create-entities [data]
  (command data))
