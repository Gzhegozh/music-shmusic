(ns music-shmusic.tracks)

(require '[music-shmusic.artists :as a]
         '[music-shmusic.releases :as r]
         '[music-shmusic.db.api :as d])


(defn create-track [data]
  (def artists (a/find-artists-by-name (:artist data)))
  (d/create-entities [{:track/name (:name data)
                       :track/duration (:duration data)
                       :track/position (:position data)
                       :track/artists artists}]))

(defn all-tracks []
  (d/query '[:find [?track ...]
             :where [?track :track/name]]))

(defn find-tracks-by-artist [artist-name]
  (d/query '[:find [?track ...]
             :in $ ?artist-name
             :where [?artist :artist/name ?artist-name]
             [?track :track/artists ?artist]]
           artist-name))

(defn get-track-by-id [id]
  (d/entity-attrs id))
