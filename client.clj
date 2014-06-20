	(require '[clojure.java.io :refer [file output-stream input-stream]])
  (import '[java.io File])


(defn sameFileProperties?
	[fileA fileB]
	"compares 2 files and returns true if they are the same and false otherwise"
	(if (= fileA fileB)
		true
		false))

(defn fileSize [filename]
  (.length (File. filename)))

(defn modified
  [fileName]
  (.lastModified (File. fileName)))

(defn getFileProperties
	[fileName]
	"Returns a map of the file properties"
	(merge {} {:size (fileSize fileName)
             :name fileName
             :modified (modified fileName)
             }))

(defn getFilesInFolder
  [folderName]
  "Returns a list of filenames in a folder given as a string"
  )

(defn printFilesInDirectory
  [dir]
  (let [d (File. dir)]
  (println "Files in " (.getName d))
  (doseq [f (.listFiles d)]
    (if (.isDirectory f)
      (print "d ")
      (print "- "))
    (println (.getName f)))))

(defn getListOfFilesInDirectory
  [dir]
  "Returns a list of filenames in a directory given as a string."
  (let [d (File. dir)
        listFiles (.listFiles d)]
    (map #(.getName %) listFiles)))

(defn getMapFileNameToFileProperties
  [folderName]
  "Returns a map of filename to file properties map."
  (let [fileNames (getListOfFilesInDirectory folderName)]
    (if (= '() fileNames)
    {}
    (loop [map {}
           i 0]
      (when (< i (- (count fileNames) 1))
        (recur (merge map {(nth fileNames i)
                           (getFileProperties (nth fileNames i))})
               (inc i))
        map)))))
   ;; (map #(merge {} {% (getFileProperties %)}) fileNames))))

;(def f1 (getFileProperties "EulerNew.jar"))

;(def f2 (getFileProperties "README.md"))

;(println (sameFileProperties? (getFileProperties "EulerNew.jar") (getFileProperties "README.md")))

;(printFilesInDirectory "../gCloud")

;(println (getListOfFilesInDirectory "../gCloud/gd"))

(println (getMapFileNameToFileProperties "../gCloud"))

