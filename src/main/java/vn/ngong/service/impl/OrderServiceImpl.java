package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.config.ShareConfig;
import vn.ngong.dto.GetListOrderDto;
import vn.ngong.dto.OrderDto;
import vn.ngong.entity.OrderDetail;
import vn.ngong.entity.Orders;
import vn.ngong.entity.PaymentMethod;
import vn.ngong.entity.Product;
import vn.ngong.kiotviet.obj.OrderCustomer;
import vn.ngong.kiotviet.obj.OrderDelivery;
import vn.ngong.kiotviet.obj.OrderPartnerDelivery;
import vn.ngong.kiotviet.obj.OrderPayment;
import vn.ngong.kiotviet.request.CreateOrdersRequest;
import vn.ngong.kiotviet.request.GetOrderKiotVietRequest;
import vn.ngong.kiotviet.response.*;
import vn.ngong.kiotviet.service.KiotVietService;
import vn.ngong.service.OrderService;
import vn.ngong.service.ProductService;

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
    @Autowired
    private ProductService productService;

    @Override
    public CreateOrdersResponse addOrderToKiotViet(Orders orders, List<OrderDetail> orderDetails, PaymentMethod paymentMethod, CreateCustomerResponse customer) {
        try {
            Timestamp deliveryTime = new Timestamp(ZonedDateTime.of(LocalDateTime.now().plusHours(3),
                    ZoneId.systemDefault()).toInstant().toEpochMilli());
            List<vn.ngong.kiotviet.obj.OrderDetail> orderDetailKiotViets = new ArrayList<>();
            int totalDiscountKiot = 0;
            for (OrderDetail orderDetail : orderDetails) {
                int currentTotalDisCount = orderDetail.getAmount() - orderDetail.getAmountDiscount();
                totalDiscountKiot += currentTotalDisCount;
                DetailProductKiotVietResponse res = kiotVietService.getDetailProductByCode(orderDetail.getProductCode());
                orderDetailKiotViets.add(vn.ngong.kiotviet.obj.OrderDetail.builder()
                        .productId(res.getId())
                        .productCode(orderDetail.getProductCode())
                        .productName(orderDetail.getProductName())
                        .quantity(orderDetail.getQuantity())
                        .price(orderDetail.getAmount())
                        .discount(currentTotalDisCount)
                        .discountRatio(0)
                        .note(orderDetail.getNote())
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
//            orderPayments.add(OrderVoucherPayment.builder()
//                    .build());
            CreateOrdersRequest rq = CreateOrdersRequest.builder()
                    .isApplyVoucher(false)
                    .purchaseDate(new Timestamp(System.currentTimeMillis()))
                    .branchId(shareConfig.getBranchId())
                    .soldById(shareConfig.getSoldByid())
                    .cashierId(shareConfig.getCashierId())
                    .discount(totalDiscountKiot)
                    .description("Đặt đơn hàng mới")
                    .method("ngong.vn")
                    .totalPayment(0)
                    //                .accountId(0)
                    .makeInvoice(false)
                    //                .saleChannelId()
                    .orderDetails(orderDetailKiotViets)
                    .orderDelivery(OrderDelivery.builder()
                            .deliveryCode("DELIVERY0001")
                            .type(0)
                            .price(orders.getTotalAmount())
                            .receiver(customer.getData().getName())
                            .contactNumber(customer.getData().getContactNumber())
                            .address(customer.getData().getAddress())
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
                            .id(customer.getData().getId())
                            .code(customer.getData().getCode())
                            .name(customer.getData().getName())
                            .gender(true)
                            .birthDate(new Timestamp(System.currentTimeMillis()))
                            .contactNumber(customer.getData().getContactNumber())
                            .address(customer.getData().getAddress())
                            .wardName(customer.getData().getAddress())
                            .email(customer.getData().getEmail())
                            .comments(customer.getData().getComments())
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

    @Override
    public GetListOrderDto getOrderList(String customerCode, String customerName, int status) {
        List<OrderDto> orderList = new ArrayList<>();
        try {
            GetOrderKiotVietRequest rq = GetOrderKiotVietRequest.builder()
                    .customerCode(customerCode)
                    .includePayment(true)
                    .includeOrderDelivery(true)
                    .pageSize(20)
                    .orderBy("createdDate")
                    .orderDirection("DESC")
                    .build();
            if (status != 0) {
                if (status == 2) {
                    rq.setStatus(new Integer[] {status, 5});
                } else {
                    rq.setStatus(new Integer[] {status});
                }
            }
            GetOrderKiotVietResponse res = kiotVietService.getOrderByCustomerCode(rq);
            if (res != null && res.getData() != null) {
                GetListOrderDto orderDto = GetListOrderDto.builder()
                        .customerCode(customerCode)
                        .customerName(customerName)
                        .total(res.getTotal())
                        .pageSize(res.getPageSize())
                        .build();
                for (DataOrderResponse order : res.getData()) {
                    List<vn.ngong.kiotviet.obj.OrderDetail> orderDetails = order.getOrderDetails();
                    for (vn.ngong.kiotviet.obj.OrderDetail od : orderDetails) {
                        Product p = productService.findById((int) od.getProductId());
                        String productImage = p != null ? p.getImage() : "";
                        od.setProductImage(productImage);
                    }
                    orderList.add(OrderDto.builder()
                            .orderCode(order.getCode())
                            .purchaseDate(order.getPurchaseDate())
                            .status(order.getStatus())
                            .statusValue(order.getStatusValue())
                            .description(order.getDescription())
                            .totalAmount(order.getTotal())
                            .createdDate(order.getCreatedDate())
                            .modifiedDate(order.getModifiedDate())
                            .orderDetails(order.getOrderDetails())
                            .build());
                }
                orderDto.setOrders(orderList);
                return orderDto;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
