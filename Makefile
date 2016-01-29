DREGEXJAR := $(HOME)/.m2/repository/com/github/marianobarrios/dregex_2.11/0.2-SNAPSHOT/dregex_2.11-0.2-SNAPSHOT.jar


install: $(DREGEXJAR)
	lein uberjar


$(DREGEXJAR):
	cd dregex && sbt publish


