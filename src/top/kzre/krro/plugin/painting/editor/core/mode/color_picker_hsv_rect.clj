(ns top.kzre.krro.plugin.painting.editor.core.mode.color-picker-hsv-rect
  "颜色选择器模式：色相条 + SV 矩形。"
  (:require [top.kzre.krro.core.core :as core]))

(defn- layout-fn [frame]
  [:block {:direction :vertical
           :alignment :center
           :style {:padding "16px" :spacing "12px"}}
   ;; 色相条（仅显示 H）
   [:krro.painting/h-ramp]
   ;; SV 矩形
   [:krro.painting/sv-rect]
   ])

(defn mount []
  (core/define-major-mode
    :krro.painting/color-picker-hsv-rect
    :name "颜色选择器（H+SV）"
    :layout layout-fn
    ))