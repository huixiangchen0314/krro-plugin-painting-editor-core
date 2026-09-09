(ns top.kzre.krro.plugin.painting.editor.core.mode.color-picker-wheel-hsv
  "颜色选择器模式：色轮 + 完整 HSV 滑动条。"
  (:require [top.kzre.krro.core.core :as core]))

(defn- layout-fn [frame]
  [:block {:direction :vertical
           :alignment :top-center}
   ;; 色轮
   [:krro.painting/color-wheel {}]
   ;; HSV 三条滑动条
   [:krro.painting/hsv-ramp {}]])

(defn mount []
  (core/define-major-mode
    :krro.painting/color-picker-wheel-hsv
    :name "颜色选择器（色轮）"
    :parent :krro.painting/basic
    :layout layout-fn
    ))