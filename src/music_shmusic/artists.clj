(ns music-shmusic.artists)

(require '[music-shmusic.db.api :as d]
         '[music-shmusic.utils :as u])

(defn create-artist [attrs]
  (def namespaced-attrs (u/namespaced-hashmap "artist" attrs))
  (d/create-entities [namespaced-attrs]))

(defn all-artists []
  (d/query '[:find [?e ...]
             :where [?e :artist/name]]))

(defn find-artists-by-name [artist-name]
  (d/query '[:find [?e ...]
             :in $ ?artist-name
             :where [?e :artist/name ?artist-name]]
           artist-name))

(defn get-artist-by-id [id]
  (d/entity-attrs id))
 
