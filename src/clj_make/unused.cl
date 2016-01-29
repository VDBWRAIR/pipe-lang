;(def-programs '(wget tar unzip cp echo))
;{ 
; :zipd [url] (wget url) 
; :f ([#".*\.gz"  :is zipd] (tar "-x" "-f" zipd)
;     [#".*\.zip" :is zipd] (unzip zipd)) 
; :cp [f :guard #(-> % .getSize (> 100000))] ;copy in chunks
;     [f :guard #(-> % .getSize (> 0))] (cp f dest)
; :fail [f] (echo "file %s is size 0") ;here we go beyond what make can do because we branched to a new rule
; :done [cp] (.exit System 0)
;       [fail] (.exit System 1)
; }
;(pos? 0)
;
;{
; :f (line-seq fn)
; :fastq  [[#"@.*" & lines] :is f] (do (print "fastq") f)
; :fasta [[#">.*" & liines] :is f] (do (print "fasta" ) f)
; :bad  [f] (.exit System 1)
; :good [fasta] ;process fasta file
;       [fastq] ;process fastq file
; }
;{ 
; :fastq  [[#"@.*" & lines] :is f] (do (print "fastq") f)
; :fasta [[#">.*" & liines] :is f] (do (print "fasta" ) f)
; } 
;;; total make-land
;{
; #"*\.bam" [#"*\.sam" :as f] (samtools "view" "-b")
; #"*\.bai" [#"*\.bai" :as f] (samtools sort f)
;:align #{".bam" ".bai"} (do stuff) ;multiple requirements
;}
;{ :auth [user] (get-auth user)
; :res [auth :guard auth-ok?]  (get "/resource") 
; :succes [{:status 400} :is res] ;; do more 
; :fail ([auth] (print "failed at auth")
;        [res] (print "faied at GET"))
;}
;
;
;;let funk be some hybrid of graph and core.match/defun
;;which pattern matches.
;{ 
; :auth   (fnk [user] (get-auth user))
; :res    (funk [auth :guard auth-ok?]  (get "/resource"))
; :succes (funk [{:status 400} :is res]) ;; do more 
; :fail   (funk ([auth] (print "failed at auth"))
;              ([res] (print "faied at GET")))
;}
;{ 
; :f "some-executable"
; :lines (line-seq fn)
; :python  [[#"*/python" & xs] :is lines] (do (print "python!")  )
; :bash        [[#"*/sh" & xs] :is lines] (do (print "bash!" ) )
; :?  [f] (.exit System 1)
; :run  [python f] (sh "python arg1 arg2" f)
;       [bash f] (sh "bash" "args" f)
; :suc [0 :is run] 
; :fail [run]
; }
;
;;; all mapvals have an implicit self variable that lets them alter the map and live dangerously?
;;;
;;; Like bpipe, files are (optionally) automatically named. The :key for the function which produces that output. 
;;;
;;; where functions are populated with magic variables $out and $in? 
;;; but in $in should be the incoming function actually.
;;; $out = $in.:key
;;;
;
;:freebayes (funk [bam ref] (freebayes bam ref $out.vcf))
;;; $out => $bam.freebayes.vcf
;; nah, should be
;:freebayes (funk [bam] (freebayes bam bam.freebayes.vcf))
;
;;; Can simply add the parts together like BPipe by extracting the funks (and loosening the pattern matching?)
;
;
;
;
;(defn $M [sym kw] (kw (meta sym)))
;;
;[:sum [a b c] +]
;;
;[:sorted [#"*\.bam"] samtools-sort ]
;;
;[
;   ["upaired.fastq" [not-paired-re] (fn [a] (when-not (do (cat :out $@) (file-size $@)) ^{:empty true})) 
;   ["merged.bam" ["p.fq" "un.fq"] (fun ([p {:meta {:empty true}}] (bwa-mem p))
;                                       ([p un] (let [outs (map #(-ext % "bam"))]
;                                                 (do (map bwa-mem [p un] outs) 
;                                                     (apply samtools-merge outs)))))
;
;   ["single.bam" ["p.fq"] #(bwa-mem> % %o)]
;
;; populate the env w/ the target
;; apply the prereqs to the function
;(defn has-match ;;([tgts func] ;; treat func like an fnk.
;  ([tgts reqs func]
;    (let [matchers (:arglist (meta #'func)) 
;      match? (atom true)]
;      (do (try (map (fn [xs] (match args xs identity) matchers)) ;i think ths requires unpaking the list?
;          (catch Exception e (swap! match? not)))
;            match?))))
;(concat #{\a \b \c}  [\a \b])
;(defn exec-next ([[] _] nil)
;  ([all-rules done-targets] 
;   (let [dones (concat (ls) done-targets)
;         rules (filter (fn [rs] (clojure.set/subset? (set r) (set dones))) all-rules) ;; also need to catch regexes here
;        [tgts reqs func] (first (filter (comp apply has-match) rules))]
;     ^{:info (apply func reqs)} tgts))) 
;;TODO: remove the returned rule from the rules which will get passed to exec-next
;;; and add it to dones
;    ;;
;(defn try-match [matcher args] zip
;  (try (match matcher args))
;(let [target (fst rule)
;      func ]
;  (try
;    (match 
;(defn foo [a b] (+ a b))
;(meta #'foo)
;(try 
;  catch
;
;(mapply (fn [a b ] (str a b  ) ) {:a 1 }) 
;
;(apply + [1 2 3] )
;
;(apply #(+ %n) [1 2 3])
;
(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
;
;

