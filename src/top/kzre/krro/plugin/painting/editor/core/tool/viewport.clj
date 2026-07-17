(ns top.kzre.krro.plugin.painting.editor.core.tool.viewport
  (:require
    [top.kzre.krro.plugin.painting.core.tool.protocol :as tb]
    [top.kzre.krro.plugin.painting.editor.core.viewport :as vp]
    [top.kzre.krro.core.custom :as custom]))

;; ── 自定义配置 ──────────────────────────────────
(custom/defcustom :krro.painting/viewport-pan-speed
                  1.0
                  :type :number
                  :group :krro.painting/edit
                  :doc "视口平移速度，1.0 为像素同比速度。")

(custom/defcustom :krro.painting/viewport-pan-dead-zone
                  0.5
                  :type :number
                  :group :krro.painting/edit
                  :doc "视口平移死区（像素），位移小于此值时忽略移动。")

(custom/defcustom :krro.painting/viewport-zoom-sensitivity
                  1.005
                  :type :number
                  :group :krro.painting/edit
                  :doc "滚轮缩放灵敏度，表示每一格 delta 的缩放因子。1.1 表示每格放大 10%。")

;; ── 工具实现 ──────────────────────────────────
(defrecord ViewPortTool [initial-mouse   ;; atom {:x :y}
                         last-event     ;; atom 最后一次事件
                         init-viewport  ;; atom 初始视口
                         moving]        ;; atom boolean
  tb/ITool
  (begin! [this layer ctx])
  (end! [this layer ctx])

  (apply! [this layer event ctx]
    (let [btn (:mouse-button event)
          type (:type event)]
      (when (= btn :middle)
        (case type
          :press
          (do
            (reset! initial-mouse {:x (:x event) :y (:y event)})
            (reset! init-viewport (vp/get-viewport (:frame ctx)))
            (reset! moving true)
            (reset! last-event event)
            :idle)

          :drag
          (when @moving
            (let [{:keys [x y]} @initial-mouse
                  dx (- (:x event) x)
                  dy (- (:y event) y)
                  ;; 读取配置
                  speed (custom/get-custom :krro.painting/viewport-pan-speed (:frame ctx))
                  dead-zone (custom/get-custom :krro.painting/viewport-pan-dead-zone (:frame ctx))
                  ;; 应用速度
                  dx' (* speed dx)
                  dy' (* speed dy)
                  ;; 死区判断
                  _ (when (and (< (Math/abs (double dx')) dead-zone)
                               (< (Math/abs (double dy')) dead-zone))
                      ;; 位移太小，不做任何视口更新
                      (throw (Exception. "dead-zone")))
                  {:keys [offset-x offset-y zoom]} @init-viewport
                  new-offset-x (- offset-x (/ dx' zoom))
                  new-offset-y (- offset-y (/ dy' zoom))]
              (try
                (vp/set-viewport! (:frame ctx)
                                  (assoc @init-viewport
                                    :offset-x new-offset-x
                                    :offset-y new-offset-y))
                (catch Exception e
                  (when (not= (.getMessage e) "dead-zone")
                    (throw e))))
              (reset! last-event event)
              :continue))

          :scroll
          (let [delta-y (:delta-y event)
                sensitivity (custom/get-custom :krro.painting/viewport-zoom-sensitivity (:frame ctx))
                factor (Math/pow sensitivity (double delta-y))
                frame (:frame ctx)]
            (vp/zoom-at-point! frame (:x event) (:y event) factor)
            (reset! last-event event)
            :continue)

          :release
          (do
            (reset! moving false)
            (reset! last-event event)
            :idle)

          :idle))))

  (preview! [this layer ctx] nil)
  (commit! [this layer ctx] nil))

(defn make-viewport-tool
  "创建视口操作工具，内部 atom 自动初始化。"
  []
  (->ViewPortTool (atom nil)    ;; initial-mouse
                  (atom nil)    ;; last-event
                  (atom nil)    ;; init-viewport
                  (atom false))) ;; moving