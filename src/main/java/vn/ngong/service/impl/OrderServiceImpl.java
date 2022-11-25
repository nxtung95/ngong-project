package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.entity.OrderDetail;
import vn.ngong.entity.Orders;
import vn.ngong.entity.PaymentMethod;
import vn.ngong.kiotviet.request.CreateOrdersRequest;
import vn.ngong.kiotviet.service.KiotVietService;
import vn.ngong.service.OrderService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private KiotVietService kiotVietService;

    @Override
    public void addOrderToKiotViet(Orders orders, List<OrderDetail> orderDetails, PaymentMethod paymentMethod) {
        List<vn.ngong.kiotviet.obj.OrderDetail> orderDetailKiotViets = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {

        }
        CreateOrdersRequest rq = CreateOrdersRequest.builder()
                .isApplyVoucher(false)
                .purchaseDate(new Timestamp(System.currentTimeMillis()))
                .branchId(1)
                .soldById(1)
                .cashierId(1)
                .discount(orders.getDiscountAmount())
                .description("Đặt đơn hàng mới")
                .method(paymentMethod.getName())
                .totalPayment(orders.getTotalAmount())
//                .accountId(0)
                .makeInvoice(false)
//                .saleChannelId()

                .build();
    }
}
