/**
 * 跳转工具模块
 * 统一管理小程序所有页面跳转，解决以下问题：
 * 1. switchTab 不支持传参 → 用事件总线（uni.$emit/$on）中转
 * 2. navigateTo 不能跳 tabBar 页 → 自动判断并切换跳转方式
 * 3. 统一跳转写法，方便后续维护
 */

/** 所有 tabBar 页面的路径集合，用于判断是否需要用 switchTab */
const TAB_BAR_PAGES = new Set([
  '/pages/index/index',
  '/pages/doctor/index',
  '/pages/news/index',
  '/pages/mine/index'
])

/**
 * 判断一个路径是否属于 tabBar 页面
 * @param url 页面路径（可带参数）
 */
function isTabBarPage(url: string): boolean {
  const path = url.split('?')[0]
  return TAB_BAR_PAGES.has(path)
}

/**
 * 跳转到指定页面，自动判断用 navigateTo 还是 switchTab
 * 如果目标是 tabBar 页面，会把参数通过事件总线传给目标页
 *
 * @param url   目标页面路径，如 '/pages/doctor/detail?id=1'
 * @param eventName 可选，事件名（用于 tabBar 传参，默认用页面路径）
 */
export function goTo(url: string, eventName?: string): void {
  if (isTabBarPage(url)) {
    // tabBar 页面必须用 switchTab，但 switchTab 不支持 ?key=value 传参
    // 所以先把参数通过事件总线发出去，再跳转
    const [path, queryString] = url.split('?')
    const params: Record<string, string> = {}

    if (queryString) {
      queryString.split('&').forEach((pair) => {
        const [k, v] = pair.split('=')
        if (k) params[decodeURIComponent(k)] = decodeURIComponent(v || '')
      })
    }

    // 用事件总线传递参数，目标页在 onLoad 里接收
    const evt = eventName || path
    uni.$emit(evt, params)

    uni.switchTab({ url: path })
  } else {
    uni.navigateTo({ url })
  }
}

/**
 * 返回上一页
 * @param delta 返回层数，默认 1
 */
export function goBack(delta = 1): void {
  uni.navigateBack({ delta })
}

/**
 * 在目标页面（tabBar 页）的 onLoad 中调用，接收从 goTo 传来的参数
 *
 * @param eventName 事件名，要和 goTo 里传的一致
 * @param callback  收到参数后的回调
 *
 * 使用示例：
 *   onLoad(() => {
 *     receiveTabParams('/pages/doctor/index', (params) => {
 *       if (params.deptId) currentDept.value = Number(params.deptId)
 *     })
 *   })
 */
export function receiveTabParams(
  eventName: string,
  callback: (params: Record<string, string>) => void
): void {
  uni.$once(eventName, callback)
}
