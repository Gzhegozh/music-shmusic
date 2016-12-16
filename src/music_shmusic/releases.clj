(ns music-shmusic.releases)

(require '[music-shmusic.artists :as a]
         '[music-shmusic.db.api :as d])


(defn create-release [data]
  (def artists (a/find-artists-by-name (:artist data)))
  (def country (:country data))
   
  (d/create-entities [{:release/name (:name data)
                       :release/year (:year data)
                       :release/month (:month data)
                       :release/day (:day data)
                       :release/country [:country/name country]
                       :release/artists artists}]))

(defn all-releases []
  (d/query '[:find [?release ...]
             :where [?release :release/name]]))

(defn find-releases-by-artist [artist]
  (d/query '[:find [?release ...]
             :in $ ?artist
             :where [?release :release/artists ?artist]]
           artist))

(defn get-release-by-id [id]
  (d/entity-attrs id))
