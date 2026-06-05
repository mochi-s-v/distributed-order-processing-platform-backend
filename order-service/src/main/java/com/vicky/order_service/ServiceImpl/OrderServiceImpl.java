package com.vicky.order_service.ServiceImpl;


import com.vicky.order_service.Client.CartClient;
import com.vicky.order_service.Client.ProductClient;
import com.vicky.order_service.Client.UserClient;
import com.vicky.order_service.Dto.RequestDto.StockDeductRequestDto;
import com.vicky.order_service.Dto.ResponseDto.CartResponseDto;
import com.vicky.order_service.Dto.ResponseDto.OrderResponseDto;
import com.vicky.order_service.Entity.OrderEntity;
import com.vicky.order_service.Entity.OrderItemsEntity;
import com.vicky.order_service.Mapper.OrderMapper;
import com.vicky.order_service.Repository.OrderRepository;
import com.vicky.order_service.Service.OrderService;
import com.vicky.order_service.Utility.GetAttributesFromHeader;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final CartClient cartClient;
    private final ProductClient productClient;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserClient userClient,
                            CartClient cartClient,
                            ProductClient productClient,
                            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.cartClient = cartClient;
        this.productClient = productClient;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderResponseDto checkout(long addressId) {
        CartResponseDto cartDto = cartClient.getCart().getData();
        if (cartDto.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }

        try {
            boolean isAddressValid = userClient.getAddressByIdInternal(addressId).getData();
            if (!isAddressValid) throw new RuntimeException();
        } catch (Exception r) {
            throw new RuntimeException("Invalid address or address does not belong to this user");
        }

        OrderEntity order = new OrderEntity();
        order.setOrderedAt(LocalDateTime.now());
        order.setTotalAmount(cartDto.getTotalCartPrice());
        order.setOrderStatus("PENDING");
        order.setUsername(GetAttributesFromHeader.getAuthUsername());
        order.setAddressId(addressId);

        List<OrderItemsEntity> orderItems = cartDto.getItems().stream()
                .map(item -> {
                    OrderItemsEntity orderItem = new OrderItemsEntity();
                    orderItem.setProductId(item.getProductId());
                    orderItem.setProductName(item.getProductName());
                    orderItem.setPriceAtPurchase(item.getPrice());
                    orderItem.setOrderCount(item.getQuantity());
                    orderItem.setSubTotal(item.getSubTotal());
                    orderItem.setOrderEntity(order);
                    return orderItem;
                }).collect(Collectors.toList());
        order.setOrderItems(orderItems);

        List<StockDeductRequestDto> stockDeductRequests = cartDto.getItems().stream()
                .map(item -> new StockDeductRequestDto(item.getProductId(), item.getQuantity()))
                .collect(Collectors.toList());

        try {
            productClient.deductStock(stockDeductRequests);
        } catch (feign.FeignException e) {
            throw new RuntimeException("Checkout failed: Insufficient stock or invalid item details.");
        } catch (Exception e) {
            throw new RuntimeException("Checkout failed: Product service is currently unreachable.");
        }

        orderRepository.save(order);
        cartClient.clearCart();
        return orderMapper.toDto(order);
    }

    @Override
    public OrderResponseDto getOrderById(long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        if (order.getUsername().equals(GetAttributesFromHeader.getAuthUsername())) {
            return orderMapper.toDto(order);
        } else {
            throw new RuntimeException("Access denied");
        }
    }

    @Override
    public List<OrderResponseDto> getAllOrdersByUserId() {
        List<OrderEntity> orders = orderRepository.findByUsername(GetAttributesFromHeader.getAuthUsername());
        return orders.stream()
                .map(orderEntity -> orderMapper.toDto(orderEntity))
                .toList();
    }
}
