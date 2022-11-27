package vn.ngong.service;

import vn.ngong.dto.GetListOrderDto;
import vn.ngong.entity.OrderDetail;
import vn.ngong.entity.Orders;
import vn.ngong.entity.PaymentMethod;
import vn.ngong.kiotviet.response.CreateCustomerResponse;
import vn.ngong.kiotviet.response.CreateOrdersResponse;

import java.util.List;

public interface OrderService {
    CreateOrdersResponse addOrderToKiotViet(Orders orders, List<OrderDetail> orderDetails, PaymentMethod paymentMethod, CreateCustomerResponse customer);

    GetListOrderDto getOrderList(String customerCode, String customerName, int status);
}
