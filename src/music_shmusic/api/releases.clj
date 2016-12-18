(ns music-shmusic.api.releases
  (:require [ring.util.response :as ring-r]
            [music-shmusic.releases :as a]
            [music-shmusic.releases :as r]
            [music-shmusic.tracks :as t]))

;; http path /api/releases/get?_=[{"id": <release-id>}] or
;; http path /api/releases/get?_=[{"name": <release-name>}] or
;; http path /api/release/get
(defn get [params]
  (if (contains? params :id)
    (ring-r/response (r/get-release-by-id (:id params)))
    (do
      (if (contains? params :name)
        (def releases-ids (r/find-releases-by-name (:name params))) 
        (def releases-ids (r/all-releases)))
      (ring-r/response (map r/get-release-by-id releases-ids)))))

;; http path /api/releases/get_tracks?_=[{"id": <release-id>}]
(defn get_tracks [params]
  (if (contains? params :id)
    (do
      (def tracks
        (->> (:id params)
             (t/find-tracks-by-release)
             (map t/get-track-by-id)
             (map #(dissoc % :release/releases))))
      (ring-r/response tracks))
    (ring-r/response {})))

;; http path /api/releases/delete?_=[{"id": <release-id>}]
(defn delete! [params]
  (if (contains? params :id)
    (do
      (r/delete-release (:id params))
      (ring-r/response {}))
    (ring-r/status (ring-r/response {:error "Please, supply an id"})
                   400)))

;; http path /api/releases/create?_=[<hash-map of attrs of the release>]
(defn create! [params]
  (r/create-release params)
  (ring-r/response {}))

;; http path /api/releases/update?_=[{"id": <release-id>, <attrs of the release to set or update>}]
(defn update! [params]
  (if (contains? params :id)
    (do
      (r/update-release (:id params) (dissoc params :id))
      (ring-r/response {}))
    (ring-r/status (ring-r/response {:error "Please, supply an id"})
                   400)))

;; http path /api/releases/comments?_=[{"id": <release-id>}]
(defn comments [params]
  (if (contains? params :id)
    (ring-r/response (r/get-release-comments (:id params)))
    (ring-r/response {})))
