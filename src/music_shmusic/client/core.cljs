(ns music-shmusic.client.core
  (:require
   [reagent.core :as r]))

(defonce click-count (r/atom 0))

(defn counting-component []
  [:div
   "The atom " [:code "click-count"] " has value: "
   @click-count ". "
   [:input {:type "button" :value "Click me!"
            :on-click #(swap! click-count inc)}]])

(defn main-component []
  [counting-component])

(defn render-app []
  (r/render-component
   [main-component]
   (js/document.getElementById "application")))

(defn init []
  (render-app))

(defn on-reload []
  (render-app))
