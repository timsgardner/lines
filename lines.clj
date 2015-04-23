(ns lines.lines
  (:require [arcadia.core :as arc])
  (:import [UnityEngine Vector3 LineRenderer GameObject]))

(defmacro ^:private def-compget [name typesym]
  (let [objsym (gensym "obj_")]
    `(defn ~name ~(with-meta [objsym] {:tag typesym})
       (arc/get-component ~objsym ~typesym))))

(def-compget line-renderer UnityEngine.LineRenderer)

(defn- cludfn
  ([] nil)
  ([_] nil)
  ([_ _] nil)
  ([_ _ _] nil))

(defn set-line-renderer-verts ^LineRenderer [^LineRenderer lr, verts]
  (.SetVertexCount lr (count verts))
  (transduce
    (keep-indexed ;; should be map-indexed, transducer not in ClojureCLR yet
      (fn [i ^Vector3 v]
        (.SetPosition lr i v)))
    cludfn
    verts)
  lr)

(defn line
  (^GameObject [^Vector3 start, ^Vector3 end]
     (line start, end, 0.2))
  (^GameObject[^Vector3 start, ^Vector3 end, ^Double width]
     (let [obj (GameObject. "line")
           ^LineRenderer lnr (.AddComponent obj
                               (type-args UnityEngine.LineRenderer))]
       (set-line-renderer-verts lnr, [start, end])
       (.SetWidth lnr width, width)
       obj)))
