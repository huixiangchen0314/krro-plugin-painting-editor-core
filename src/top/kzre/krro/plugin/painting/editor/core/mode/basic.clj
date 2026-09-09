(ns top.kzre.krro.plugin.painting.editor.core.mode.basic
  "krro.painting 的基本模式，绑定了大部分通用的变量和快捷键"
  (:require [top.kzre.krro.core.core :as krro]))

(defn mount []
  (krro/define-major-mode
    :krro.painting/basic
    :name "krro.painting 基本模式"
    :keymap
    {:c :krro.painting/activate-color-picker-wheel-hsv-mode
     }
    ))
