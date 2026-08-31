(ns top.kzre.krro.plugin.painting.editor.core.overlay.overlay)

(defmulti draw-overlay
          "工具类型，工具描述，图层上下文, 其他可选数据, 事件戳,视口变换等)"
          (fn [tool _desc _gc _opt] tool))

(defmethod draw-overlay :default [_ _ _ _])