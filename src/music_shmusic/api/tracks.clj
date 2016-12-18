(ns music-shmusic.api.tracks)

(require '[ring.util.response :as ring-r]
         '[music-shmusic.tracks :as a]
         '[music-shmusic.tracks :as r]
         '[music-shmusic.tracks :as t])

;; http path /api/tracks/get?_=[{"id": <track-id>}] or
;; http path /api/tracks/get?_=[{"name": <track-name>}] or
;; http path /api/track/get
(defn get [params]
  (if (contains? params :id)
    (ring-r/response (t/get-track-by-id (:id params)))
    (do
      (if (contains? params :name)
        (def tracks-ids (t/find-tracks-by-name (:name params)))
        (def tracks-ids (t/all-tracks)))
      (ring-r/response (map t/get-track-by-id tracks-ids)))))

;; http path /api/tracks/delete?_=[{"id": <track-id>}]
(defn delete! [params]
  (if (contains? params :id)
    (do
      (t/delete-track (:id params))
      (ring-r/response {}))
    (ring-r/status (ring-r/response {:error "Please, supply an id"})
                   400)))

;; http path /api/tracks/create?_=[<hash-map of attrs of the track>]
(defn create! [params]
  (t/create-track params)
  (ring-r/response {}))

;; http path /api/tracks/update?_=[{"id": <track-id>, <attrs of the track to set or update>}]
(defn update! [params]
  (if (contains? params :id)
    (do
      (t/update-track (:id params) (dissoc params :id))
      (ring-r/response {}))
    (ring-r/status (ring-r/response {:error "Please, supply an id"})
                   400)))
