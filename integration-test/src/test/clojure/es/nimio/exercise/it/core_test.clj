(ns es.nimio.exercise.it.core-test
  (:use [es.nimio.exercise.it.core])
  (:use [clojure.test])
  (:import [cucumber.api.cli Main]))

(deftest run-features
  (. Main (main (into-array ["--format" "pretty" "--glue"
                        "src/test/resources/acceptance/step_definitions"
                        "src/test/resources/acceptance/features"]))))