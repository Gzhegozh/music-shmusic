(ns music-shmusic.client.core
  (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]

              [bidi.bidi :as bidi]
              [schema.core :as s] ;For when defining routes get tricky
              [bidi.schema]

              [accountant.core :as accountant]))

(enable-console-print!)

(defonce click-count (reagent/atom 0))

(defn sign_in[]
  ;ajax query to log in here
  )

(defn sign_up[]
  ;ajax query to sign up here
  )

(defn sign-in-form []
  [:div {:class "panel panel-default"}
    [:div {:class "panel-body"}
    [:div {:class "col-lg-3"}]
    [:div {:class "jumbotron col-lg-6"}
      [:h2 "Welcome to the Music-Shmusic!"]
     [:input {:type "text" :class "form-control" :name "email"}]
     [:br]
     [:input {:type "password" :class "form-control" :name "password"}]
     [:br]
     [:input {:type "button" :value "Sign In" :class "btn btn-primary"
            :on-click (sign_in)}]
     [:p "New guy?"]
     [:a {:href "/sign_up"} "Sign Up"]]]])

(defn sign-up-form []
  [:div {:class "panel panel-default"}
    [:div {:class "panel-body"}
    [:div {:class "col-lg-3"}]
    [:div {:class "jumbotron col-lg-6"}
      [:h2 "Sign Up to Music-Shmusic!"]
     [:input {:type "text" :class "form-control" :name "email"}]
     [:br]
     [:input {:type "password" :class "form-control" :name "password"}]
     [:br]
     [:input {:type "button" :value "Sign Up" :class "btn btn-primary"
            :on-click (sign_up)}]
     [:a {:href "/sign_in"} "Back to log in"]]]])

(defn sign-in-component []
  [sign-in-form])

(defn sign-up-component []
  [sign-up-form])

(defn render-sign-in-form []
  (reagent/render-component
   [sign-in-component]
   (js/document.getElementById "sign-in-content")))

(defn render-sign-up-form []
  (reagent/render-component
   [sign-up-component]
   (js/document.getElementById "sign-up-content")))

(defn init  []
  (render-sign-in-form)
  (render-sign-up-form))

(defn on-reload []
  (render-sign-in-form)
  (render-sign-up-form))