(ns top.kzre.krro.plugin.painting.editor.core.mode.new-image
  "新建图像模式"
  (:require [top.kzre.krro.core.core :as core]
            [top.kzre.krro.ui.core.bind :as bind]
            [taoensso.timbre :as log]
            [top.kzre.krro.core.message :as msg]))


(defn- layout-fn [frame]
  (let [state (atom {:dpi 96
                     :width 1920
                     :height 1080
                     :name "Untitled"})
        bind-ctx (bind/create state)]
    [:block {:direction :vertical
             :alignment :top-center
             :stylesheet "classpath://krro/painting/new-image.edn"
             :style {:padding "12px" :spacing "8px"}}
     ;; 画布名称
     [:block {:style {:spacing "8px" :min-width "80px"}}
      [:text {:content "Name:"}]
      [:input {:bind-ctx bind-ctx
               :bind [:name]
               :style {:pref-width "200px"}}]]

     ;; 宽度
     [:block {:style {:spacing "8px" :min-width "80px"}}
      [:text {:content "Width:"}]
      [:input {:bind-ctx bind-ctx
               :bind [:width]
               :style {:pref-width "100px"}
               :on-change (fn [e]
                            (swap! state assoc :width (Integer/parseInt (:new-value e))))}]]

     ;; 高度
     [:block {:style {:spacing "8px" :min-width "80px"}}
      [:text {:content "Height:"}]
      [:input {:bind-ctx bind-ctx
               :bind [:height]
               :style {:pref-width "100px"}}]]

     ;; 分辨率（DPI）滑块
     [:block {:style {:spacing "8px" :min-width "80px"}}
      [:text {:content "DPI:"}]
      [:slider {:min 72 :max 300
                :bind [:dpi] :bind-ctx bind-ctx
                :style {:pref-width "150px"}
                :on-change (fn [e] (swap! state assoc :dpi (int (:new-value e))))}]
      [:text {:bind-ctx bind-ctx
              :bind [:dpi]
              :style {:min-width "40px"}}]]

     ;; 分隔线
     [:separator {:style {:padding "8px 0"}}]

     ;; 按钮行（右对齐）
     [:block {:style {:spacing "8px"}}
      [:button {:content "Cancel"
                :on-click (fn [e] (log/debug "Cancel" e))}]
      [:button {:content "Create"
                :on-click (fn [e] (msg/message "Create"))}]]]))

(defn mount []
  (core/define-major-mode
    :krro.painting/new-image
    :layout layout-fn))