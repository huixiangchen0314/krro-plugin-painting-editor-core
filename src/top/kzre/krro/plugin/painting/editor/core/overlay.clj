(ns top.kzre.krro.plugin.painting.editor.core.overlay
  (:require
   [taoensso.timbre :as log]
   [taoensso.tufte :refer [p profile]]
   [top.kzre.krro.core.frame :as frame]
   [top.kzre.krro.core.reframe :as rf]
   [top.kzre.krro.plugin.painting.core.store :as store]
   [top.kzre.krro.plugin.painting.editor.core.graph :as graph]))

(defmulti draw-overlay!
          "根据描述项绘制到图形上下文。"
          (fn [desc _gc] (first desc)))

(defmethod draw-overlay! :default
  [desc _]
  (log/warn "Unknown overlay type:" (first desc)))



(rf/reg-fx
  store/app-id :tool/flush-overlay
  (fn [_ overlay-v frame]
    (profile
      {:id :krro.painting.tool/flush-overlay}
      (p :flush-overlay
         (when-let [gc (frame/param frame :krro.painting/overlay-graph-context)]
           (when overlay-v
             (graph/submit! gc
                            (fn []
                              (graph/clear! gc)
                              (doseq [overlay overlay-v]
                                (draw-overlay! overlay gc))))))))))


(defmethod draw-overlay! :cursor
  [[_ {:keys [x y type radius color]}] gc]
  (case type
    :circle
    (do
      (graph/set-stroke-color! gc color)
      (graph/set-stroke-width! gc 1.0)
      (graph/draw-oval! gc x y radius radius)
      ;; 绘制十字线
      (graph/draw-line! gc (- x radius) y (+ x radius) y)
      (graph/draw-line! gc x (- y radius) x (+ y radius)))
    nil))


(defmethod draw-overlay! :circle
  [[_ {:keys [x y radius fill-color stroke-color stroke-width stroke-dash]} ] gc]
  (when stroke-color (graph/set-stroke-color! gc stroke-color))
  (when stroke-width (graph/set-stroke-width! gc stroke-width))
  (when stroke-dash (graph/set-stroke-dash! gc stroke-dash))
  (when fill-color (graph/set-fill-color! gc fill-color))
  (graph/draw-oval! gc x y radius radius)
  (when fill-color (graph/fill-oval! gc x y radius radius)))

(defmethod draw-overlay! :rect
  [[_ {:keys [x y width height fill-color stroke-color stroke-width stroke-dash]}] gc]
  (when stroke-color (graph/set-stroke-color! gc stroke-color))
  (when stroke-width (graph/set-stroke-width! gc stroke-width))
  (when stroke-dash (graph/set-stroke-dash! gc stroke-dash))
  (when fill-color (graph/set-fill-color! gc fill-color))
  (graph/draw-rect! gc x y width height)
  (when fill-color (graph/fill-rect! gc x y width height)))