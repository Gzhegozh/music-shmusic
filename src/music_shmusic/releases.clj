(ns music-shmusic.releases
  (:require [music-shmusic.artists :as a]
            [music-shmusic.db.api :as d]
            [music-shmusic.utils :as u]))

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
  (u/remove-namespace-from-hashmap (d/entity-attrs id)))

(defn create-release [attrs]
  (def namespaced-attrs (u/namespaced-hashmap "release" attrs))
  (d/create-entities [namespaced-attrs]))
 
(defn delete-release [id]
  (d/retract-entity id))

(defn update-release [id attrs]
  (def namespaced-attrs (u/namespaced-hashmap "release" attrs))
  (d/update-entity id namespaced-attrs))

(defn comment-release [attrs]
  (def namespaced-attrs (u/namespaced-hashmap "comment" attrs))
  (d/create-entities [namespaced-attrs]))

(defn get-release-comments [release-id]
  (def comments-ids
    (d/query '[:find [?comment ...]
               :in $ ?release
               :where [?comment :comment/release ?release]]
             release-id))
  (map d/entity-attrs comments-ids))
