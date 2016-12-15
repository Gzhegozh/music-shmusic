(ns music-shmusic.releases)

(require '[music-shmusic.server :as server]
         '[music-shmusic.artists :as a]
         '[datomic.api :as d])


(defn create-release [data]
  (def artists (a/find-artists-by-name (:artist data)))
  (def country (:country data))
   
  (d/transact @server/conn
              [{:release/name (:name data)
                :release/year (:year data)
                :release/month (:month data)
                :release/day (:day data)
                :release/country [:country/name country]
                :release/artists artists}]))

(defn all-releases []
  (d/q '[:find [?release ...]
         :where [?release :release/name]]
       (d/db @server/conn)))

(defn find-releases-by-artist [artist-name]
  (d/q '[:find [?release ...]
         :in $ ?artist-name
         :where [?artist :artist/name ?artist-name]
                [?release :release/artists ?artist]]
       (d/db @server/conn)
       artist-name))

(defn get-release-by-id [id]
  (d/pull (d/db @server/conn) '[*] id))
