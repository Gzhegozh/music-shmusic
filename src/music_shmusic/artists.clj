(ns music-shmusic.artists)

(require '[music-shmusic.server :as server]
         '[datomic.api :as d])

(defn create-artist [name]
  (d/transact @server/conn
              [{:artist/name name}]))

(defn all-artists []
  (d/q '[:find [?e ...]
         :where [?e :artist/name]]
       (d/db @server/conn)))

(defn find-artists-by-name [artist-name]
  (d/q '[:find [?e ...]
         :in $ ?artist-name
         :where [?e :artist/name ?artist-name]]
       (d/db @server/conn)
       artist-name))

(defn get-artist-by-id [id]
  (d/pull (d/db @server/conn) '[*] id))
  
 
