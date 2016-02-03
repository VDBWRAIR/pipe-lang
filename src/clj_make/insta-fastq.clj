
(require ['clojure.zip :as 'z])
(require '[clojure.test :refer :all])

(def fq-parse (insta/parser (clojure.java.io/file "fastq.grammar")))
 
(def tr1
(fq-parse
"@M02261:12:000000000-A6FJ2:1:1101:15914:1452 1:N:0:12
CTTATCTCCTGTTCCACTTCAAACAGCAGTTGTAATGCTTGCATGAATGTTATTTGTTCAAAGCTATTTTCAGTTGTTCTTAATCTGTGTCTCACTTCTTCAATTAGCCATCTTATCTCTTCAAACTTCTGACCTAGCTGTTCTCGCCATTTCCCGTTTCTGTTTTGGAGTAAGTGGAGGTCCCCCATTTTCATTACTGCTTCTCCTAGCGAATCTCTGTAGATTTTTAGAGACTCGAACTGTGTT
+
1>AA1FD3DFFFG3BBF11FGGGGHHGFHGFGGHHFGBHHFHHHFFFFF1DFGHHHGF1FFH10G1HHBHFHEHHGGHHHHH2FFGHABGAGHFHHHFHHBGFHF1GBFFHHHFEGGHBGB1B@EGGHFHFEHGGFFHFFGH2EGGGCFGFHHFBE/FBGEDEBFCB?10FFHDDDHDC//@@FGE?CG2<FGGGHEHBGFHHHHH1<>C--@>CHFHDDDBD000DG./<DDGGB..:.GFHHHH
"))

(def tr2
(fq-parse
"@EAS54_6_R1_2_1_443_348
GTTGCTTCTGGCGTGGGTGGGGGGG
+
ZZZZZZZZZZZXZVZZMVZRXRRRR
"))

(defn get-encoding [t] (-> (z/vector-zip t) z/down z/right z/right z/right z/right z/node first)) 

(is
  (= :sanger (get-encoding tr1)))
'randomly' generate based on json schema
{:minimum fd/<=
{:maximum fd/>=
{:type "integer" (pred integer?)
{:type "string" (pred string?)
{:type "array" (pred list?)
{:items
(defun items [d]
 ({:minItems l )
 ({:maxItems l)
 ({
(is
  (= :illumina-1-3 (get-encoding tr2)))

(defn counto [l c]
    ((reduceo (fn [c _ c']
                              (fd/+ c 1 c')))
        0 l c))
(pred q #(c/== (count %) 5) )
(defn percento [q]
  (fd/in q (fd/interval 0 100))) 
; from https://www.refheap.com/07e1c04590b5a63af5633da36/raw
(defn reduceo [g]
  (fn reduceo* [val coll ret]
    (conde
      [(== coll ()) (== val ret)]
      [(fresh [x xs ret']
         (conso x xs coll)
         (g val x ret')
         (reduceo* ret' xs ret))])))

(defn sumo [l sum]
  ((reduceo fd/+) 0 l sum))
(let [vars (repeatedly 5 lvar)]
  (run 10 [q]
    (== q vars)
    (pred q #(c/== (count %) 5) )
    (everyg percento q)
    (sumo q 100))) )

(run 10
     [q]
  (fresh [r a b c d]
    (== q [r a b c d])
    (fd/in r a b c d (fd/interval 0 100))
    (fd/eq (= (+ a r b c d) 100))))

  (refer-clojure :exclude [==])
  (use 'clojure.core.logic))
(require '[clojure.core :as c])

   (defne counto [s n] 
         ([[] 0])
         ([[?y . ?xs] n] (counto ?xs (dec n))))
(defne zero [n] ([0]))
   (run 20 [h q]
        (percento h) ;; up here it matters
        (let [xs (llist h '())]
        (zero h)  ;; doesn't matter down here
        (== q xs)
        (everyg percento xs)
          ))
        (counto xs 2)))

   (run 10 [l sum]
                                  (counto l 10)
                                  ((everyg #(fd/dom % (fd/domain 0 1 2 3 4))) l)
                                  (sumo l sum))
(run 10 [q]
   (counto q 10))
        (everyg percento h)))
        (for [x (range 10)] (lvar 
     (fd/in q (fd/multi-interval 0 1 2 3 4 5 ))

   (require '[clojure.core.logic.fd :as fd])
(l/run 1 [q]
  (l/fresh [a b d]
     (l/== q (l/llist a b d))
     (fd/bounded-listo q 6)))
                              (fd/bound
(l/run 1 [q]
   (l/fresh [h t tt]
    (== q (l/lcons h t ))
      (l/project [q]
        (== (count q) 2))))

   ([[] 0])(counto ?xs (- n 1))))
(/defne nonemptyo [x]
   ([[?x . ?xs]] )) 
(defne samesizeo [xs ys]
   ([[] []])
   ([[?a . ?as] [z & zs]] (samesizeo ?as zs)))

(run 7 [q]
   (fresh [x y z]
   (appendo x y q)
   (everyg percento q)
   ;(c/== (lcons x y) q)
   (nonemptyo q)))
    (samesizeo q [1 2])))
(defne my-membero [e xs]
   ([e [e . _]])
   ([_ [_ . ?xs]] (my-membero e ?xs)))
(l/run 2 [q]
   (l/fresh [x]
      (== 3 x)
      (fd/+ q 1 x)))
   (> 4 0)
(run 3 [q] (my-membero 1 q) (my-membero 3 q) (firsto q :blue) (== 3 3) (lengtho 0 q))
(run 3 [q] (fresh [n] (== n 3) (my-membero 1 q) (lengtho n q)))
(run 3 [q] (fresh [n] (== n 1) (my-membero 1 q) (longero n q)))
(run 3 [q] (fresh [n] (== n 1) (my-membero 1 q) (shortero n q)))
(run 3 [q] (fresh [n] (== n 3) (my-membero 1 q) (min-lengtho n q)))
(run 2 [q] (fresh [n] (== n 3) (my-membero 1 q) (max-lengtho n q)))
(run 2 [q] (percento q))
(run 2 [q] (fresh [min max] (== min 1) (== max 3) (shortero max q) (longero min q)  (everyo  percento q) ))
(run 2 [q] (fresh [x] (== 1 x) (fd/+ x 1 q)))
(run 2 [q] (resto [1 2 3 4] q))
(run 2 [q] (== q "hed") (my-membero :a [q :b :c]))

(run 2 [q] (pred q list?))


(require '[clojure.core.match :refer [match]])
(match [{:a 1 :b 2}] [{:a 1} & r]  r) ;; not work
(apply #(run 1 [q] (fresh [x] (== x q) (% x))) [(fn [x] (membero x [12 12]))])

;;; works
  (run 12 [q] 
    (fresh [x]
      (== x q) 
      (apply-collg preds x)))


(defn applyg
  [g v]
  "Goal that succeeds when the goal g applied to v succeeds.
  Non-relational as g must be ground."
  (project [g] (everyg g [v])))
;; from http://stackoverflow.com/questions/19358461/how-to-deal-with-list-of-goals-in-core-logic
(defna apply-collg
  [gcoll v]
  ([() _])
  ([[gh . gt] _] (applyg gh v) (apply-collg gt v)))


(let [ max-int 9999999 min-int -9999999]
  (defun pred>
   ([:type "integer"] '() (fn [_] succeed))
   ([:minimum x] #(fd/in % (fd/interval x max-int)))
   ([:maximum x] #(fd/in % (fd/interval min-int x)))))

(def preds (reduce-kv 
  (fn [a k v] 
     (cons (pred> k v) a )) '() 
          {:type "integer"
           :minimum 3
           :maximum 4}))

match [obj]
(defun obj>
 [:type "integer"] (int> obj)
 [:type "array"]   (array> obj))
(use 'defun)
(defun array>
  ([:items m] '(everyo (obj> m)))
  ([:minItems x] '(longero (dec x) q))
  ([:maxItems x] '(shortero (inc x) q)))
(reduce-kv (fn [a k v] (+ a v )) 0 {:a 1 :b 3})
(apply #(run 1 [x] %)
(fresh [q]
(== q x)
 (reduce-kv 
  (fn [a k v] 
      (cons (pred> q k v) a )) '() 
           {:type "integer"
            :minimum 3
            :maximum 4})
))
(require '[defun :refer [defun]])
(let [ max-int 9999999 min-int -9999999]
  (defun pred>
   ([q :type "integer"] '())
   ([q :minimum x] (fd/in q (fd/interval x max-int)))
   ([q :maximum x] (fd/in q (fd/interval min-int x)))))

(defn pop-key [m k]
  [(dissoc m k) (k m)])
(defun int> 
 ([:minimum x] (fd/in q (fd/interval x max-int)))
 ([:maximum x] (fd/in q (fd/interval min-int x))))

match [a]
  [:items m] (prod-obj m)

                 (let [x 12] (run 2 [q] (fd/>= q x)))
                (let [x 13] (run 2 [q]  (fd/in q (fd/interval 0 13))))

[{:mutation

;; the pattern is followed by multiple clauses (a list) just like run.
(defne everyo [g l]
  ([_ ()])
  ([_ [?x . ?xs]] (g ?x) (everyo g ?xs)))

(defne max-lengtho [n l]
  ([_ [_ . ?xs]] (fresh [n1] (fd/- n 1 n1)
                    (max-lengtho n1 ?xs)))
  ([?n []] (fd/> ?n 1)))

(defne min-lengtho [n l]
  ([0 [_ . _]])
  ([_ [_ . ?xs]] (fresh [n1] (fd/- n 1 n1)
                    (min-lengtho n1 ?xs))))

(defne shortero [n l]
  ([_ [_ . ?xs]] (fresh [n1] (fd/- n 1 n1)
                    (shortero n1 ?xs)))
  ([?n []] (fd/> ?n 0)))

(defne longero [n l]
  ([0 [_ . _]])
  ([_ [_ . ?xs]] (fresh [n1] (fd/- n 1 n1)
                    (longero n1 ?xs))))
(defne lengtho [n l]
  ([0 []])
  ([?n [_ . ?xs]] (fresh [n1] (fd/- n 1 n1)
                  (lengtho n1 ?xs))))

(defnu lengtho [l n]
  ([[] 0])
  ([[_ . ?rst] _] (fresh [n1]
                        (lengtho ?rst n1)
                        (succ n1 n)))))
(defne lengtho [n l]
  ([0 []])
  ([?n [_ . ?xs]] (fresh [n1]
                  (project [n n1]
                    (== n1 (dec n)))
                  (lengtho n1 ?xs))))

(defne lengtho [n xs]
   ([0 []])
   ([n [_ . ?xs]] (lengtho (- n 1) ?xs)))
length(0,[]).
length(L+l, H|T):-length(L,T).

