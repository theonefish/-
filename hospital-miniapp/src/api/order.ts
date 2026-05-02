import { request } from '@/utils/request'

export function getUserOrders(userId: number) {
  return request<{ data: any[] }>({
    url: '/make-order/user/' + userId,
    method: 'GET'
  })
}

export function createOrder(data: any) {
  return request({
    url: '/make-order',
    method: 'POST',
    data
  })
}

export function cancelOrder(id: number) {
  return request({
    url: '/make-order/' + id,
    method: 'PUT',
    data: { status: 2 }
  })
}
