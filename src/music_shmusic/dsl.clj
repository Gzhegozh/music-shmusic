(ns music-shmusic.dsl
  (:require [clojure.edn :as edn]
            [clojure.set]))

(def command-set #{:create :delete})

(defn get-func [command-key model-name]
  (def f-ns (str "music-shmusic." model-name "s"))
  (def f-name (str (name command-key) "-" model-name))
  (resolve (symbol f-ns f-name)))

(defn do-command [command-str]
  (def command-hashmap (edn/read-string (str "{" command-str "}")))
  (if-let [command-key
           (first (clojure.set/intersection
                   (set (keys command-hashmap))
                   command-set))]
    (if (contains? command-hashmap :attrs)
      ((get-func command-key (command-key command-hashmap))
       (:attrs command-hashmap))
      nil)
    nil))
