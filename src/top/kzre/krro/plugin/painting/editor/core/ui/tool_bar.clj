(ns top.kzre.krro.plugin.painting.editor.core.ui.tool-bar
  (:require
    [top.kzre.krro.plugin.painting.core.state :as state]
    [top.kzre.krro.plugin.painting.core.project.canvas :as pc]
    [top.kzre.krro.plugin.painting.core.tool.protocol :as tp]
    [top.kzre.krro.plugin.painting.core.tool.registry :as registry])
  (:import (top.kzre.krro.plugin.painting.core.tool.brush BrushTool)
           (top.kzre.krro.plugin.painting.core.tool.move MoveTool)))

(defn- tool-id [tool-inst]
  (cond
    (instance? BrushTool tool-inst) :brush
    (instance? MoveTool tool-inst) :move
    :else nil))

(defn tool-bar-vnode [canvas-id frame]
  (let [current-tool (state/current-tool canvas-id)
        active-id    (tool-id current-tool)]
    (into [:tool-bar {:class "tool-bar"}]
          (for [{:keys [id name icon make-fn]} registry/tools]
            [:button {:key id
                      :class (str "tool-btn" (when (= id active-id) " active"))
                      :content (str icon " " name)
                      :on-click (fn [_]
                                  (let [layer        (state/selected-layer! canvas-id)
                                        data         (pc/canvas-data! canvas-id)
                                        runtime      (state/canvas-runtime canvas-id)
                                        ctx          (when (and layer data runtime)
                                                       (tp/make-context canvas-id frame data))
                                        ;; 用 atom 保持图层引用，以便在 end! 之后可以更新并传给 begin!
                                        layer-atom   (atom layer)]
                                    ;; 结束旧工具
                                    (when-let [old (state/current-tool canvas-id)]
                                      (let [{:keys [layer state]} (tp/end! old @layer-atom runtime ctx)]
                                        (when state
                                          (swap! state/canvas-runtimes assoc canvas-id state))
                                        (when layer
                                          (reset! layer-atom layer))))
                                    ;; 开始新工具
                                    (let [new-tool (make-fn)
                                          {:keys [layer state]} (tp/begin! new-tool @layer-atom runtime ctx)]
                                      (when state
                                        (swap! state/canvas-runtimes assoc canvas-id state))
                                      ;; begin! 通常返回原图层，若变化则更新
                                      (when layer
                                        (reset! layer-atom layer))
                                      (state/set-current-tool! canvas-id new-tool))))}]))))