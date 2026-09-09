(ns top.kzre.krro.plugin.painting.editor.core.core
  "绘图插件入口。"
  (:require
   [top.kzre.krro.canvas.core.core]
   [top.kzre.krro.canvas.vector.core]
   [top.kzre.krro.core.core :as krro]
   [top.kzre.krro.core.plugin :as plugin]
   [top.kzre.krro.plugin.painting.core.core]
   [top.kzre.krro.plugin.painting.core.project.canvas :as pc]
   [top.kzre.krro.plugin.painting.editor.core.mode :as mode]
   [top.kzre.krro.plugin.painting.editor.core.mode.basic :as basic]
   [top.kzre.krro.plugin.painting.editor.core.mode.color-picker-hsv-rect :as color-picker-hsv-rect]
   [top.kzre.krro.plugin.painting.editor.core.mode.color-picker-wheel-hsv :as color-picker-wheel-hsv]
   [top.kzre.krro.plugin.painting.editor.core.mode.new-image :as new-image]
   [top.kzre.krro.plugin.painting.editor.core.overlay]
   [top.kzre.krro.plugin.undo.core]
   [top.kzre.krro.ui.javafx.core]))


(krro/reg-plugin!
  {:name :krro.plugin/painting
   :mount
   (fn []
     (plugin/reg-plugin! pc/canvas-codec-plugin-def)
     (mode/mount)
     (basic/mount)
     (new-image/mount)
     (color-picker-wheel-hsv/mount)
     (color-picker-hsv-rect/mount)
     )})