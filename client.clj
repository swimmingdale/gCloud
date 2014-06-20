	(require '[clojure.java.io :refer [file output-stream input-stream]])
  (import '[java.io File])
  (use 'clojure.set)

  (def data-base-file ".gCloud")

(defn same-file-properties?
	[file-a file-b]
	"compares 2 files and returns true if they are the same and false otherwise"
	(if (= file-a file-b)
		true
		false))

(defn file-size [filename]
  (.length (File. filename)))

(defn modified
  [filename]
  (.lastModified (File. filename)))

(defn get-file-properties
	[filename]
	"Returns a map of the file properties"
	(merge {} {:size (file-size filename)
             :name filename
             :modified (modified filename)
             }))

(defn print-files-in-directory
  [dir]
  (let [d (File. dir)]
  (println "Files in " (.getName d))
  (doseq [f (.listFiles d)]
    (if (.isDirectory f)
      (print "d ")
      (print "- "))
    (println (.getName f)))))

(defn get-list-of-files-in-directory
  [dir]
  "Returns a list of filenames in a directory given as a string."
  (let [d (File. dir)
        list-files (.listFiles d)]
    (filter #(if (not (= % data-base-file))
               true
               false)
            (map #(.getName %) list-files))))

(defn get-map-filename-to-file-properties
  [folder-name]
  "Returns a map of filename to file properties."
  (let [filenames (get-list-of-files-in-directory folder-name)]
    (if (= '() filenames)
    {}
    (loop [map {}
           i 0]
      (if (< i (- (count filenames) 1))
        (recur (merge map {(nth filenames i)
                           (get-file-properties (nth filenames i))})
               (inc i))
        map)))))

(defn write-current-files-properties
  [string]
  "Writes current files properties to filename(should start with a . to be hidden in Unix-like OSs."
  (spit data-base-file string))

(defn read-current-files-properties
  []
  "Returns a map containing the metadata for the current files on gCloud."
  (read-string (slurp data-base-file)))

(defn edited-files-filter
  [old key-entry new]
  (if (same-file-properties? (get new key-entry) (get old key-entry))
    false
    true))

(defn compare-states
  [old new]
  (let [old-state-keys-set (into #{} (keys old))
        new-state-keys-set (into #{} (keys new))
        new-files (difference new-state-keys-set old-state-keys-set)
        deleted-files (difference old-state-keys-set new-state-keys-set)
        same-or-modified (difference new-state-keys-set new-files)]
        (do
          ;(println same-or-modified " - same of modified files")
          ;(println deleted-files " - deleted files")
          ;(println new-files " - new files")
        {:modified (filter #(edited-files-filter old % new) same-or-modified)
         :new new-files
         :deleted deleted-files}))
  )


   ;; (map #(merge {} {% (get-file-properties %)}) filenames))))

;(def f1 (get-file-properties "EulerNew.jar"))

;(def f2 (get-file-properties "README.md"))

;(println (same-file-properties? (get-file-properties "EulerNew.jar") (get-file-properties "README.md")))

;(print-files-in-directory "../gCloud")

;(println (get-list-of-files-in-directory "../gCloud/gd"))

;(write-current-files-properties (get-map-filename-to-file-properties "../gCloud"))

;(println (read-current-files-properties))

(println (compare-states {"server.clj" {:modified 1403179340000, :name "server.clj", :size 21}, "g" {:modified 1403301825000, :name "g", :size 4096}, "hj" {:modified 1403301830000, :name "hj", :size 0}, "README.md" {:modified 1403176142000, :name "README.md", :size 141}, "client.clj" {:modified 1403301936000, :name "client.clj", :size 3582}} (get-map-filename-to-file-properties "../gCloud") ))


;(println (difference #{:a :b :c} #{:c :d :b}))
