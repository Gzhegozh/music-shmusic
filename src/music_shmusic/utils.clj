(ns music-shmusic.utils
  (:require [clojure.set :refer [rename-keys]]))

  
(defn namespaced-hashmap [namespace hashmap]
  (def old-keys (keys hashmap))
  (def new-keys (map #(keyword namespace (name %))
                     old-keys))
  (rename-keys hashmap (zipmap old-keys new-keys)))
