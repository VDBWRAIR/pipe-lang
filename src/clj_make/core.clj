(ns clj-make.core 
  (:require [t6.from-scala.core :refer ($ $$) :as $])
  (:import (dregex Regex)))

(defn snd [tup] (let [[s r] ($/view tup)] r))
(defn fst [tup] (let [[s r] ($/view tup)] s))

(defn re-subset [A B] (not (.matchesAnything (.diff A B))))

(defn re-pair [s1 s2] (let [xs ($ List & s1 s2)
                         [a b] ($/view ($ Regex/compile xs))
                         [A B] (map snd [a b])] [A B]))

(defn str-subset [A B] (apply re-subset (re-pair A B)))


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
