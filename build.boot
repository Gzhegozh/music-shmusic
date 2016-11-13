(set-env!
 :dependencies
 '[[org.clojure/clojure "1.8.0"]
   [ring "1.5.0"]
   ;; [org.clojure/tools.nrepl "0.2.12"]
   [org.clojars.gjahad/debug-repl "0.3.3"]
   [http-kit "2.2.0"]
   [com.datomic/datomic-free "0.9.5404"]
   [mount "0.1.10"]]
 :source-paths #{"src"})

(apply
 require '[[music-shmusic.core :as ms]
           [org.httpkit.server :as http-kit]
           [ring.middleware.reload :as reload]])

(deftask dev []
  (comp
   (fn [x]
     (println "Starting server")
     (http-kit/run-server (reload/wrap-reload ms/app) {:port 3000})
     (println "Started server")
     x)
   (repl :server true)
   (wait)))
