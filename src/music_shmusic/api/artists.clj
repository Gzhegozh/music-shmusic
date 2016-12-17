(ns music-shmusic.api.artists)

(require '[ring.util.response :as ring-r]
         '[music-shmusic.artists :as a]
         '[music-shmusic.releases :as r]
         '[music-shmusic.tracks :as t])

;; http path /api/artists/get?_=[{"id": <artist-id>}] or
;; http path /api/artists/get
(defn get [params]
  (if (contains? params :id)
    (ring-r/response (a/get-artist-by-id (:id params)))
    (do
      (def artists-ids (a/all-artists))
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
