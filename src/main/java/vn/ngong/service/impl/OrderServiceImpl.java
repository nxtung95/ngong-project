package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.config.ShareConfig;
import vn.ngong.dto.payment.TransCustomerDto;
import vn.ngong.entity.Customer;
import vn.ngong.entity.OrderDetail;
import vn.ngong.entity.Orders;
import vn.ngong.entity.PaymentMethod;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.kiotviet.obj.*;
import vn.ngong.kiotviet.request.CreateOrdersRequest;
import vn.ngong.kiotviet.response.CreateOrdersResponse;
import vn.ngong.kiotviet.response.DetailProductKiotVietResponse;
import vn.ngong.kiotviet.service.KiotVietService;
import vn.ngong.service.OrderService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private KiotVietService kiotVietService;
    @Autowired
    private ShareConfig shareConfig;

    @Override
    public CreateOrdersResponse addOrderToKiotViet(Orders orders, List<OrderDetail> orderDetails, PaymentMethod paymentMethod, Customer customer) {
        try {
            Timestamp deliveryTime = new Timestamp(ZonedDateTime.of(LocalDateTime.now().plusHours(3),
                    ZoneId.systemDefault()).toInstant().toEpochMilli());
            List<vn.ngong.kiotviet.obj.OrderDetail> orderDetailKiotViets = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetails) {
                DetailProductKiotVietResponse res = kiotVietService.getDetailProductByCode(orderDetail.getProductCode());
                orderDetailKiotViets.add(vn.ngong.kiotviet.obj.OrderDetail.builder()
                        .productId(res.getId())
                        .productCode(orderDetail.getProductCode())
                        .productName(orderDetail.getProductName())
                        .quantity(orderDetail.getQuantity())
                        .price(orderDetail.getAmount())
                        .discount(orderDetail.getAmount() - orderDetail.getAmountDiscount())
                        .discountRatio(0)
                        .note("Đặt hàng từ ngong.vn")
                        .build());
            }
            List<Object> orderPayments = new ArrayList<>();
            orderPayments.add(OrderPayment.builder()
                    .id(1)
                    .code(paymentMethod.getId())
                    .amount(orders.getTotalAmount())
                    .method(paymentMethod.getName())
                    .status(1)
                    .statusValue("Thanh toán thành công")
                    .transDate(orders.getCreatedDate())
                    .build());
            orderPayments.add(OrderVoucherPayment.builder()
                    .build());
            CreateOrdersRequest rq = CreateOrdersRequest.builder()
                    .isApplyVoucher(false)
                    .purchaseDate(new Timestamp(System.currentTimeMillis()))
                    .branchId(shareConfig.getBranchId())
                    .soldById(shareConfig.getSoldByid())
                    .cashierId(shareConfig.getCashierId())
                    .discount(orders.getDiscountAmount())
                    .description("Đặt đơn hàng mới")
                    .method("ngong.vn")
                    .totalPayment(orders.getTotalAmount())
                    //                .accountId(0)
                    .makeInvoice(false)
                    //                .saleChannelId()
                    .orderDetails(orderDetailKiotViets)
                    .orderDelivery(OrderDelivery.builder()
                            .deliveryCode("DELIVERY0001")
                            .type(0)
                            .price(orders.getTotalAmount())
                            .receiver(customer.getName())
                            .contactNumber(customer.getPhone())
                            .address(customer.getAddress())
                            .locationId(240)
                            .locationName("Hà Nội - Quận Cầu Giấy")
                            .wardName("Phường Nghĩa Tân")
                            .partnerDeliveryId(147011)
                            .expectedDelivery(deliveryTime)
                            .partnerDelivery(OrderPartnerDelivery.builder()
                                    .code("DELIVERY0001")
                                    .name("Đối tác 0001")
                                    .address("HN")
                                    .contactNumber("0123456789")
                                    .email("delivery001@gmail.com")
                                    .build())
                            .build())
                    .customer(OrderCustomer.builder()
                            .id(shareConfig.getCustomerId())
                            .code(shareConfig.getCustomerCode())
                            .name(customer.getName())
                            .gender(true)
                            .birthDate(new Timestamp(System.currentTimeMillis()))
                            .contactNumber(customer.getPhone())
                            .address(customer.getAddress())
                            .wardName(customer.getAddress())
                            .email(customer.getEmail())
                            .comments(customer.getNote())
                            .build())
                    .surchages(new ArrayList<>())
                    .payments(orderPayments)
                    .build();
            CreateOrdersResponse res = kiotVietService.createOrders(rq);
            if (res != null) {
                if (res.getResponseStatus() != null) {
                    return null;
                }
            }
            return res;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
