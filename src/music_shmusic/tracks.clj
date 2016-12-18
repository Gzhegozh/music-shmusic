(ns music-shmusic.tracks)

(require '[music-shmusic.artists :as a]
         '[music-shmusic.releases :as r]
         '[music-shmusic.db.api :as d]
         '[music-shmusic.utils :as u])

(defn all-tracks []
  (d/query '[:find [?track ...]
             :where [?track :track/name]]))

(defn find-tracks-by-name [track-name]
  (d/query '[:find [?track ...]
             :in $ ?track-name
             :where [?track (fulltext $ :track/name ?track-name)]]
           track-name))

(defn find-tracks-by-artist [artist]
  (d/query '[:find [?track ...]
             :in $ ?artist
             :where [?track :track/artists ?artist]]
           artist))

(defn find-tracks-by-release [release]
  (d/query '[:find [?tracks ...]
             :in $ ?release
             :where [?release :release/tracks ?tracks]]
           release))

(defn get-track-by-id [id]
  (d/entity-attrs id))

(defn create-track [attrs]
  (def namespaced-attrs (u/namespaced-hashmap "track" attrs))
  (d/create-entities [namespaced-attrs]))
 
(defn delete-track [id]
  (d/retract-entity id))

(defn update-track [id attrs]
  (def namespaced-attrs (u/namespaced-hashmap "track" attrs))
  (d/update-entity id namespaced-attrs))
