(ns top.kzre.krro.plugin.painting.editor.core.mode.color-picker-wheel-hsv
  "颜色选择器模式：色轮 + 完整 HSV 滑动条。"
  (:require
    [top.kzre.krro.core.core :as core]
    [top.kzre.krro.plugin.painting.core.brush.core :as brush]
    [top.kzre.krro.ui.core.bind :as bind]
    [top.kzre.krro.ui.core.core :as krro.ui]
    [top.kzre.krro.plugin.painting.core.brush.global :as global]))

(defn- factory [init-props]
  {:render
   (fn [_props frame]
     (let [bind-ctx (bind/create global/global-brush)]
       [:block {:direction :vertical
                :alignment :top-center}
        [:krro.painting/color-wheel
         {:bind [:color]
          :bind-ctx bind-ctx
          :krro.painting/on-color-selected
          (fn [c]
            (brush/set-global-brush-color! c))}]
        [:krro.painting/hsv-ramp
         {:bind [:color]
          :bind-ctx bind-ctx
          :krro.painting/on-color-selected
          (fn [c]
            (brush/set-global-brush-color! c))}]]))})

(defn mount []
  (krro.ui/reg-component :krro.painting/color-picker-wheel-hsv factory)
  (core/define-major-mode
    :krro.painting/color-picker-wheel-hsv
    :name "颜色选择器（色轮）"
    :parent :krro.painting/basic
    :layout [:krro.painting/color-picker-wheel-hsv]
    ))