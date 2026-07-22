(ns top.kzre.krro.plugin.painting.editor.core.tool.viewport
  (:require
    [top.kzre.krro.core.custom :as custom]
    [top.kzre.krro.plugin.painting.core.tool.protocol :as tb]
    [top.kzre.krro.plugin.painting.core.viewport :as vp]))

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

(defrecord ViewPortTool [initial-mouse   ;; atom {:x :y}
                         last-event     ;; atom 最后一次事件
                         init-viewport  ;; atom 初始视口
                         moving]        ;; atom boolean
  tb/ITool
  (id [_] :viewport)
  (overlay [_] nil)
  (begin! [_ layer state ctx]
    {:layer layer :state state})
  (end! [_ layer state ctx]
    {:layer layer :state state})

  (apply! [_ layer state event ctx]
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
                  speed (custom/get-custom :krro.painting/viewport-pan-speed (:frame ctx))
                  dead-zone (custom/get-custom :krro.painting/viewport-pan-dead-zone (:frame ctx))
                  dx' (* speed dx)
                  dy' (* speed dy)
                  _ (when (and (< (Math/abs (double dx')) dead-zone)
                               (< (Math/abs (double dy')) dead-zone))
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

  (preview! [_ layer state ctx]
    {:layer layer :state (assoc state :dirty-tiles nil)})
  (commit! [_ layer state ctx]
    ;; 视口操作需要全图刷新，因此将 dirty-tiles 设为 nil
    {:layer layer :state (assoc state :dirty-tiles nil)}))

(defn make-viewport-tool []
  (->ViewPortTool (atom nil) (atom nil) (atom nil) (atom false)))