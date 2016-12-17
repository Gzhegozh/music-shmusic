(ns music-shmusic.config
  (:require [clojure.java.io :as io])
  (:require [datomic.api :as d]))

(defn make-path [& name-parts]
  (.getAbsolutePath (apply io/file name-parts)))

;; project can't be ran from any other directory than it's root
;; (boot won't see its build.boot), so it is safe to assume that
;; current directory is the project directory
(def project-dir (make-path ""))
(println "Project dir is" project-dir)
(def webroot-dir (make-path project-dir "www-root"))
(def target-dir  (make-path webroot-dir "target"))

(def db-uri "datomic:dev://localhost:14334/ms")
(def vk-app-id (System/getenv "VK_APP_ID"))
