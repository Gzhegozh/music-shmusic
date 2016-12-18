(ns music-shmusic.artists)

(require '[music-shmusic.db.api :as d]
         '[music-shmusic.utils :as u])

(defn all-artists []
  (d/query '[:find [?artist ...]
             :where [?artist :artist/name]]))

(defn find-artists-by-name [artist-name]
  (d/query '[:find [?artist ...]
             :in $ ?artist-name
             :where [(fulltext $ :artist/name ?artist-name)
                     [[?artist ?name ?tx ?score]]]]
           artist-name))

(defn get-artist-by-id [id]
  (d/entity-attrs id))
 
(defn create-artist [attrs]
  (def namespaced-attrs (u/namespaced-hashmap "artist" attrs))
  (d/create-entities [namespaced-attrs]))

(defn delete-artist [id]
  (d/retract-entity id))

(defn update-artist [id attrs]
  (d/update-entity id attrs))
