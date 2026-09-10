(ns top.kzre.krro.plugin.painting.editor.core.ui.menu-bar
  "菜单栏")

(defn menu-bar [frame]
  [:menu-bar {:class "main-menu"
              :style {:max-height "25px"}
              :grow false}
   [:menu {:content "文件"}
    [:menu-item {:content "新建" :on-action :krro.file/new}]
    [:menu-item {:content "打开..." :on-action :krro.file/open}]
    [:menu-item {:content "保存" :on-action :krro.file/save}]
    [:menu-item {:content "另存为..." :on-action :krro.file/save-as}]
    [:menu-separator]
    [:menu-item {:content "导出为..." :on-action :krro.file/export}]
    [:menu-item {:content "关闭" :on-action :krro.file/close}]]

   [:menu {:content "编辑"}
    [:menu-item {:content "撤销" :on-action :krro.edit/undo}]
    [:menu-item {:content "重做" :on-action :krro.edit/redo}]
    [:menu-separator]
    [:menu-item {:content "剪切" :on-action :krro.edit/cut}]
    [:menu-item {:content "复制" :on-action :krro.edit/copy}]
    [:menu-item {:content "粘贴" :on-action :krro.edit/paste}]
    [:menu-separator]
    [:menu-item {:content "首选项..." :on-action :krro.edit/preferences}]]

   [:menu {:content "视图"}
    [:menu {:content "缩放"}
     [:menu-item {:content "放大" :on-action :krro.view/zoom-in}]
     [:menu-item {:content "缩小" :on-action :krro.view/zoom-out}]
     [:menu-item {:content "适应画布" :on-action :krro.view/fit}]]
    [:menu-separator]
    [:menu-item {:content "显示标尺" :checked true :on-action :krro.view/toggle-rulers}]
    [:menu-item {:content "显示网格" :checked false :on-action :krro.view/toggle-grid}]]

   [:menu {:content "图像"}
    [:menu-item {:content "调整大小..." :on-action :krro.image/resize}]
    [:menu-item {:content "画布大小..." :on-action :krro.image/canvas-size}]
    [:menu {:content "旋转"}
     [:menu-item {:content "顺时针 90°" :on-action :krro.image/rotate-cw}]
     [:menu-item {:content "逆时针 90°" :on-action :krro.image/rotate-ccw}]
     [:menu-item {:content "自定义..." :on-action :krro.image/rotate-custom}]]]

   [:menu {:content "图层"}
    [:menu-item {:content "新建图层" :on-action :krro.layer/new}]
    [:menu-item {:content "复制图层" :on-action :krro.layer/duplicate}]
    [:menu-item {:content "删除图层" :on-action :krro.layer/delete}]
    [:menu-separator]
    [:menu-item {:content "合并可见图层" :on-action :krro.layer/merge-visible}]
    [:menu-item {:content "向下合并" :on-action :krro.layer/merge-down}]]

   [:menu {:content "工具"}
    [:menu-item {:content "笔刷" :on-action :krro.tool/brush}]
    [:menu-item {:content "橡皮擦" :on-action :krro.tool/eraser}]
    [:menu-item {:content "填充" :on-action :krro.tool/fill}]
    [:menu {:content "选择"}
     [:menu-item {:content "矩形选择" :on-action :krro.tool/select-rect}]
     [:menu-item {:content "椭圆选择" :on-action :krro.tool/select-ellipse}]
     [:menu-item {:content "套索" :on-action :krro.tool/select-lasso}]]]])