// H5 开发环境使用代理，小程序/APP 使用真实地址
const BASE_URL = import.meta.env.DEV && typeof window !== 'undefined'
  ? '/api'
  : 'http://localhost:8080/api'

interface RequestOptions {
  url: string
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
  data?: any
  header?: Record<string, string>
}

export function request<T = any>(options: RequestOptions): Promise<T> {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('token') || ''

    // 过滤掉 undefined 和 null 的参数，避免传给后端导致类型转换错误
    let cleanData: any = {}
    if (options.data) {
      for (const key in options.data) {
        const val = options.data[key]
        if (val !== undefined && val !== null && val !== '') {
          cleanData[key] = val
        }
      }
    }

    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: cleanData,
      header: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token,
        ...options.header
      },
      success: (res: any) => {
        if (res.statusCode === 200 && res.data.code === 200) {
          resolve(res.data)
        } else if (res.statusCode === 401 || res.data.code === 401) {
          // 401 错误交给调用方处理，不强制跳转登录
          reject({ ...res.data, needLogin: true })
        } else {
          uni.showToast({ title: res.data.message || '请求失败', icon: 'none' })
          reject(res.data)
        }
      },
      fail: (err) => {
        uni.showToast({ title: '网络请求失败', icon: 'none' })
        reject(err)
      }
    })
  })
}

export function uploadFile(url: string, filePath: string, name = 'file'): Promise<any> {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('token') || ''
    uni.uploadFile({
      url: BASE_URL + url,
      filePath,
      name,
      header: {
        'Authorization': 'Bearer ' + token
      },
      success: (res: any) => {
        const data = JSON.parse(res.data)
        if (data.code === 200) {
          resolve(data)
        } else {
          uni.showToast({ title: data.message || '上传失败', icon: 'none' })
          reject(data)
        }
      },
      fail: (err) => {
        uni.showToast({ title: '上传失败', icon: 'none' })
        reject(err)
      }
    })
  })
}
