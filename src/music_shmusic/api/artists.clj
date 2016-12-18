(ns music-shmusic.api.artists)

(require '[ring.util.response :as ring-r]
         '[music-shmusic.artists :as a]
         '[music-shmusic.releases :as r]
         '[music-shmusic.tracks :as t])

;; http path /api/artists/get?_=[{"id": <artist-id>}] or
;; http path /api/artists/get?_=[{"name": <artist-name>}] or
;; http path /api/artists/get
(defn get [params]
  (if (contains? params :id)
    (ring-r/response (a/get-artist-by-id (:id params)))
    (do
      (if (contains? params :name)
        (def artists-ids (a/find-artists-by-name (:name params)))
        (def artists-ids (a/all-artists)))
      (ring-r/response (map a/get-artist-by-id artists-ids)))))

;; http path /api/artists/get_releases?_=[{"id": <artist-id>}]
(defn get_releases [params]
  (if (contains? params :id)
    (do
      (def releases
        (->> (:id params)
             (r/find-releases-by-artist)
             (map r/get-release-by-id)
             (map #(dissoc % :release/artists))))
      ;; (println releases)
      (ring-r/response releases))
    (ring-r/response {})))

;; http path /api/artists/get_tracks?_=[{"id": <artist-id>}]
(defn get_tracks [params]
  (if (contains? params :id)
    (do
      (def tracks
        (->> (:id params)
             (t/find-tracks-by-artist)
             (map t/get-track-by-id)
             (map #(dissoc % :release/artists))))
      (ring-r/response tracks))
    (ring-r/response {})))

;; http path /api/artists/delete?_=[{"id": <artist-id>}]
(defn delete! [params]
  (if (contains? params :id)
    (do
      (a/delete-artist (:id params))
      (ring-r/response {}))
    (ring-r/status (ring-r/response {:error "Please, supply an id"})
                   400)))

;; http path /api/artists/create?_=[<hash-map of artist's attrs>]
(defn create! [params]
  (a/create-artist params)
  (ring-r/response {}))

;; http path /api/artists/update?_=[{"id": <artist-id>, <artist's attrs to set or update>}]
(defn update! [params]
  (if (contains? params :id)
    (do
      (a/update-artist (:id params) (dissoc params :id))
      (ring-r/response {}))
    (ring-r/status (ring-r/response {:error "Please, supply an id"})
                   400)))
