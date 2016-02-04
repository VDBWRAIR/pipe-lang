(ns clj-make.core 
  (:require [t6.from-scala.core :refer ($ $$) :as $])
  (:import (dregex Regex)))

(defn snd [tup] (let [[s r] ($/view tup)] r))
(defn fst [tup] (let [[s r] ($/view tup)] s))

(defn re-subset [A B] (not (.matchesAnything (.diff A B))))

(defn re-pair [s1 s2] 
  (let [xs ($ List & s1 s2)
       [a b] ($/view ($ Regex/compile xs))
       [A B] (map snd [a b])] [A B]))

(defn str-subset [A B] (apply re-subset (re-pair A B)))
(defun append [xs ys]
(use 'defun)

(defun append 
  ([[x & xs] ys] (cons x (append xs ys)))
  ([[] ys]  ys))
  "ret is the result of appending x and y"
(defne appendo- [x y ret]
  ([[?x . ?xs] ?ys ?ret] 
    (fresh [middle]
      (appendo- ?xs ?ys middle)
      (conso ?x middle ?ret)))
  ([() ?ys ?ret] (== ?ys ?ret))) ;; note this acts as accumulating a constraint onto ret.
;; instead of returning values, we unify it with the output argument!
  ;;
(defne same-lengtho [xs ret]
  ([() ()])
  ([[?x . ?xs] [y? . ?ys]] (same-lengtho ?xs ?ys)))
 ;; once it matches, then determines if the statement is true! binding . . . 
  ;; succeed in conde is like else in cond (but conde tries everything anyway. conde tries every branch
(comment
  (assert  (= (run 2 [q] (same-lengtho [1 2 3] q)) '((_0 _1 _2))))
    )
 (apply list [1 2 3])
(append [3] [4 5])
;; (!=  not equal unifier
;; conde-- pick any of, just like defne.
;; fresh-- a goal constructor, can have any number of goals
;; cons x y ret--ret is equal to cons of x an dy
(run 3 [q] (appendo- [1] [3 4] q))
(run 3 [x y] (appendo- [x] [3 y] [x 3 x]))
(run 1 [q] (conso 1 [2 3 4] q))
(reduce append [[1 2 3] [4 5] [6]])
(append [1 2 3] [3 4 5])
  ([a b] `(~a ~b)))
(run 1 [q] (== q 2.0) (pred q integer?))
(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
  (refer-clojure :exclude [==])
  (use 'clojure.core.logic))
(require '[clojure.core :as c])
