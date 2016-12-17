(set-env!
 :repositories
 #(conj %
        ["my-datomic" {:url "https://my.datomic.com/repo"
                       :username (System/getenv "DATOMIC_USERNAME")
                       :password (System/getenv "DATOMIC_PASSWORD")}])

 :dependencies
 '[[org.clojure/clojure "1.8.0"]
   [org.clojure/clojurescript "1.9.293"]
   [adzerk/boot-cljs "1.7.228-2"]
   [adzerk/boot-reload "0.4.13"]
   [adzerk/boot-cljs-repl   "0.3.3"] ;; latest release
   [com.cemerick/piggieback "0.2.1"  :scope "test"]
   [weasel                  "0.7.0"  :scope "test"]
   [org.clojure/tools.nrepl "0.2.12" :scope "test"]

   ;; app:
   [ring "1.5.0"]
   [ring/ring-json "0.4.0"]
   [bidi "2.0.14"]
   [http-kit "2.2.0"]
   [com.datomic/datomic-pro "0.9.5544"]
   [reagent "0.6.0"]
   [gravatar "1.1.0"]
   [clojure-vk "0.1.0-SNAPSHOT"]
   [noapi "1.0.0"]
   [mount "0.1.10"]]
 :source-paths #{"src"}
 :resource-paths #{"resources"})

(apply
 require '[[adzerk.boot-cljs :as cljs]
           [adzerk.boot-reload :as cljs-reload]
           [adzerk.boot-cljs-repl :as cljs-repl]

           [mount.core :as mount]
           [music-shmusic.db.api :as db]
           [music-shmusic.server :as server] ;; to register mount states
           [music-shmusic.config :as config]])

(deftask dev []
  (db/init-conn)
  (comp
   (fn [x]
     (println "Starting server")
     (mount/start)
     (println "Started server")
     x)
   (repl :server true)
   (watch)
   (cljs-reload/reload
    :cljs-asset-path "/target"
    :on-jsload 'music-shmusic.client.core/on-reload)
   (cljs-repl/cljs-repl)
   (cljs/cljs :compiler-options {:asset-path "/target/app.out"})
   (target :dir #{config/target-dir})))
