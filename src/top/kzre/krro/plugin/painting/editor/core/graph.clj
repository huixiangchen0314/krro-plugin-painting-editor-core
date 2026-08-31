(ns top.kzre.krro.plugin.painting.editor.core.graph)

(defprotocol IGraphicsContext
  "抽象图形上下文，用于 overlay 绘制，与具体 UI 框架解耦。"

  ;; ---- 清除与画布信息 ----
  (clear! [this] "清除整个绘制区域。")
  (get-width [this] "获取画布宽度。")
  (get-height [this] "获取画布高度。")

  ;; ---- 路径构建 ----
  (begin-path! [this] "开始新路径。")
  (move-to! [this x y] "移动当前点到 (x, y)。")
  (line-to! [this x y] "从当前点画直线到 (x, y)。")
  (quadratic-curve-to! [this cpx cpy x y] "二次贝塞尔曲线。")
  (bezier-curve-to! [this cp1x cp1y cp2x cp2y x y] "三次贝塞尔曲线。")
  (close-path! [this] "闭合当前路径。")
  (stroke-path! [this] "使用当前 stroke 样式绘制路径。")
  (fill-path! [this] "使用当前 fill 样式填充路径。")
  (clip-path! [this] "将当前路径设为裁剪区域。")

  ;; ---- 直接绘制（快捷方式） ----
  (draw-line! [this x1 y1 x2 y2] "绘制直线。")
  (draw-rect! [this x y w h] "绘制矩形边框。")
  (fill-rect! [this x y w h] "填充矩形。")
  (draw-oval! [this x y rx ry] "绘制椭圆边框。")
  (fill-oval! [this x y rx ry] "填充椭圆。")
  (draw-text! [this text x y] "绘制文本（当前字体、填充颜色）。")

  ;; ---- 样式设置 ----
  (set-stroke-color! [this color] "设置描边颜色。")
  (set-stroke-width! [this width] "设置描边宽度。")
  (set-stroke-dash! [this dash-array] "设置虚线模式（如 [5,5]）。")
  (set-stroke-dash-offset! [this offset] "设置虚线偏移。")
  (set-fill-color! [this color] "设置填充颜色。")
  (set-font! [this font] "设置文本字体。")

  ;; ---- 状态管理 ----
  (save! [this] "保存当前图形上下文状态。")
  (restore! [this] "恢复最近保存的状态。")

  ;; ---- 变换（可选） ----
  (translate! [this tx ty] "平移变换。")
  (scale! [this sx sy] "缩放变换。")
  (rotate! [this angle] "旋转变换（弧度）。")
  )