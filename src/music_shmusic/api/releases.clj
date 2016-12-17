(ns music-shmusic.api.releases)

(require '[ring.util.response :as ring-r]
         '[music-shmusic.releases :as a]
         '[music-shmusic.releases :as r]
         '[music-shmusic.tracks :as t])

;; http path /api/releases/get?_=[{"id": <release-id>}] or
;; http path /api/release/get
(defn get [params]
  (if (contains? params :id)
    (ring-r/response (r/get-release-by-id (:id params)))
    (do
      (def releases-ids (r/all-releases))
      (ring-r/response (map r/get-release-by-id releases-ids)))))

;; http path /api/releases/get_tracks?_=[{"id": <release-id>}]
(defn get_tracks [params]
  (if (contains? params :id)
    (do
      (def tracks
        (->> (:id params)
             (t/find-tracks-by-release)
             (map t/get-track-by-id)
             (map #(dissoc % :release/artists))))
      (ring-r/response tracks))
    (ring-r/response {})))
