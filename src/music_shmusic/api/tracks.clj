(ns music-shmusic.api.tracks)

(require '[ring.util.response :as ring-r]
         '[music-shmusic.tracks :as a]
         '[music-shmusic.tracks :as r]
         '[music-shmusic.tracks :as t])

;; http path /api/tracks/get?_=[{"id": <track-id>}] or
;; http path /api/track/get
(defn get [params]
  (if (contains? params :id)
    (ring-r/response (t/get-track-by-id (:id params)))
    (do
      (def tracks-ids (t/all-tracks))
      (ring-r/response (map t/get-track-by-id tracks-ids)))))
