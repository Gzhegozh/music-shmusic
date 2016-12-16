(ns music-shmusic.artists)

(require '[music-shmusic.db.api :as d])

(defn create-artist [name]
  (d/create-entities [{:artist/name name}]))

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
 
