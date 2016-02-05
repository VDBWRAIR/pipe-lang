(use 'instagenerate.core)
(require ['clojure.zip :as 'z])
(require '[clojure.core.logic.fd :as fd])
(require '[clojure.test :refer :all])
(require '[instaparse.core :as insta])
  (use 'clojure.core.logic))
(require '[clojure.core :as c])
  (refer-clojure :exclude [==])
(defn print-gen [s](map println (map #(apply str %) s)))
(println (apply str (first (generate-possible-strings fq-parse ))))
(println (apply str (first (generate-strings-for-parse-tree fq-parse tr2))))
(def fq-parse (insta/parser (clojure.java.io/file "fastq.grammar")))
 

(def tr2
(fq-parse
"@id1
GTTGCTTCTGGCGTGGGTGGGGGGG
+
ZZZZZZZZZZZXZVZZMVZRXRRRR"))
(defn producto [coll ret]
  (everyo #(membero % (vec coll)) ret))
(defn sequenceo [ret]
  (everyo #(membero % (map str (vec "ACGTN"))) ret))
(run 3 [q]
  (sequenceo q)
  ;(everyo #(membero % (vec "ACGTN")) q)
  (lengtho 2 q)
  (membero \N q)) ; note how N is being used "backwards" here
(def sanger (map str '(\! \" \# \$ \% \& \\ \( \) \* \+ \, \- \. \/ \0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \: \; \< \= \> \? \@ \A \B \C \D \E \F \G \H \I \J \K \L \M \N \O \P \Q \R \S \T \U \V \W \X \Y \Z \[ \\\ \] \^ \_ \` \a \b \c \d \e \f \g \h \i \j \k \l \m \n \o \p \q \r \s \t \u \v \w \x \y \z \{ \| \})))
(def NT "ACGTN-")
(defne everyo [g l]
  ([_ ()])
  ([_ [?x . ?xs]] (g ?x) (everyo g ?xs)))
(generate-strings-for-parse-tree fq-parse [:file [:sangerRec [:header "id" "3"] [:nt "A"] "+" [:sanger "!"]]])

(fq-parse
"@id1
GTTGCTTCTGGCGTGGGTGGGGGGG
+
!!!!!!!!!!!!!!!!ttttttttt")
(generate-strings-for-parse-tree fq-parse [:file [:sangerRec [:header "id" "3"] "A" "+" "!"]])

(generate-strings-for-parse-tree fq-parse [:file [:sangerRec '(:header "id" "0" "0" "0") "A" "+" "!"]])

(run 1 [q] (sequenceo q) (sangero q) (longero 4 q))

;;TODO: why so slow?
(print-gen 
(generate-strings-for-parse-tree fq-parse 
(first
(run 1 [?]
  (fresh [s q qc sc hd id p1 p2 rec]
    (producto (map str (range 10)) id)
    (longero 0 id)
    (appendo [:sangerRec hd] s p1)
    (appendo p1 p2 rec)
    ;;(conjo p1 "+" p2) ;; why doesn't work?
    (conso "+" q p2)
    (appendo [:header "id"] id hd)
    (== ? [:file rec])
    (sequenceo s)
    (sangero q) ; could wrap in function & put constraints here
    (same-lengtho s q)
    (membero "N" s) ; could put constraints here
    (longero 1 s)
         ))
          )                       ))
(def sangero (partial producto sanger))
(defne lengtho [n l]
  ([0 []])
  ([?n [_ . ?xs]] (fresh [n1] (fd/- n 1 n1)
                  (lengtho n1 ?xs))))

(defne same-lengtho [xs ret]
  ([() ()])
  ([[?x . ?xs] [y? . ?ys]] (same-lengtho ?xs ?ys)))

(defne longero [n l]
  ([0 [_ . _]])
  ([_ [_ . ?xs]] (fresh [n1] (fd/- n 1 n1)
                    (longero n1 ?xs))))


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

