(ns top.kzre.krro.plugin.painting.editor.core.ui.tool-bar
  (:require
   [top.kzre.krro.core.reframe :as rf]
   [top.kzre.krro.plugin.painting.core.edit.tools :as tools]
   [top.kzre.krro.plugin.painting.core.state :as state]
   [top.kzre.krro.plugin.painting.core.store :as store]))


(defn tool-bar-vnode [canvas-id frame]
  ;; TODO 替换为查询
  (let [current-tool (state/current-tool canvas-id)]
    (into [:tool-bar {:class "tool-bar"
                      :max-height 50}]
          (for [{:keys [id name icon]} (tools/tools)]
            [:button {:key id
                      :class (str "tool-btn" (when (= id current-tool) " active"))
                      :content (str icon " " name)
                      :on-click (fn [_]
                                  (rf/dispatch store/app-id [:tool/select-tool canvas-id id]))}]))))