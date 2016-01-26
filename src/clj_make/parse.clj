(require '[instaparse.core :as insta])
(def parse (insta/parser (slurp "src/clj_make/make.grammar") :auto-whitespace :standard))
(parse (slurp "src/clj_make/example.mk")) 
