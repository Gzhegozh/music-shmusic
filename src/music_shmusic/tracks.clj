(ns music-shmusic.tracks)

(require '[music-shmusic.server :as server]
         '[music-shmusic.artists :as a]
         '[music-shmusic.releases :as r]
         '[datomic.api :as d])


(defn create-track [data]
  (def artists (a/find-artists-by-name (:artist data)))
  (d/transact @server/conn
              [{:track/name (:name data)
                :track/duration (:duration data)
                :track/position (:position data)
                :track/artists artists}]))

(defn all-tracks []
  (d/q '[:find [?track ...]
         :where [?track :track/name]]
       (d/db @server/conn)))

(defn find-tracks-by-artist [artist-name]
  (d/q '[:find [?track ...]
         :in $ ?artist-name
         :where [?artist :artist/name ?artist-name]
                [?track :track/artists ?artist]]
       (d/db @server/conn)
       artist-name))

(defn get-track-by-id [id]
  (d/pull (d/db @server/conn) '[*] id))
