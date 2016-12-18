(ns music-shmusic.releases)

(require '[music-shmusic.artists :as a]
         '[music-shmusic.db.api :as d]
         '[music-shmusic.utils :as u])

(defn all-releases []
  (d/query '[:find [?release ...]
             :where [?release :release/name]]))

(defn find-releases-by-name [release-name]
  (d/query '[:find [?release ...]
             :in $ ?release-name
             :where [(fulltext $ :release/name ?release-name)
                     [[?release ?name ?tx ?scope]]]]
           release-name))

(defn find-releases-by-artist [artist]
  (d/query '[:find [?release ...]
             :in $ ?artist
             :where [?release :release/artists ?artist]]
           artist))

(defn get-release-by-id [id]
  (d/entity-attrs id))

(defn create-release [attrs]
  (def namespaced-attrs (u/namespaced-hashmap "release" attrs))
  (d/create-entities [namespaced-attrs]))
 
(defn delete-release [id]
  (d/retract-entity id))

(defn update-release [id attrs]
  (d/update-entity id attrs))
