package com.vicky.cart_service.ServiceImpl;


import com.vicky.cart_service.Client.ProductClient;
import com.vicky.cart_service.Dto.RequestDto.CartRequestDto;
import com.vicky.cart_service.Dto.ResponseDto.ApiResponse;
import com.vicky.cart_service.Dto.ResponseDto.CartResponseDto;
import com.vicky.cart_service.Dto.ResponseDto.ProductResponseDto;
import com.vicky.cart_service.Entity.CartEntity;
import com.vicky.cart_service.Entity.CartItemEntity;
import com.vicky.cart_service.Mapper.CartMapper;
import com.vicky.cart_service.Repository.CartItemsRepository;
import com.vicky.cart_service.Repository.CartRepository;
import com.vicky.cart_service.Service.CartService;
import com.vicky.cart_service.Utility.GetAttributesFromHeader;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final ProductClient productClient;
    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemsRepository cartItemsRepository,
                           ProductClient productClient,
                           CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.productClient = productClient;
        this.cartMapper = cartMapper;
    }

    @Override
    @Transactional
    public CartResponseDto addItemToCart(CartRequestDto cartRequestDto) {
        CartEntity cart = cartRepository.findByUsername(GetAttributesFromHeader.getAuthUsername())
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUsername(GetAttributesFromHeader.getAuthUsername());
                    newCart.setCreatedAt(LocalDateTime.now());
                    return cartRepository.save(newCart);
                });

        ApiResponse<ProductResponseDto> response = productClient.getProduct(cartRequestDto.getProductId());
        ProductResponseDto product = response.getData();
        System.out.println(product.toString());

        CartItemEntity cartItem = cartItemsRepository
                .findByCartEntity_IdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        if (cartRequestDto.getQuantity() > product.getQuantity()) {
            throw new RuntimeException("quantity is limited");
        }

        if (cartItem == null) {
            cartItem = new CartItemEntity();
            cartItem.setCartEntity(cart);
            cartItem.setProductId(product.getId());
            cartItem.setQuantity(cartRequestDto.getQuantity());
            cartItem.setCreatedAt(LocalDateTime.now());
        } else {
            int targetQuantity = cartItem.getQuantity() + cartRequestDto.getQuantity();
            if (targetQuantity > product.getQuantity()) {
                throw new RuntimeException("Cannot add more items. Total exceeds available stock.");
            }
            cartItem.setQuantity(targetQuantity);
        }
        cartItemsRepository.save(cartItem);
        return getCart();
    }

    @Override
    @Transactional
    public CartResponseDto updateItemQuantity(long productId, int newQuantity) {
        if (newQuantity <= 0) {
            return removeItemFromCart(productId);
        }

        CartEntity cart = cartRepository.findByUsername(GetAttributesFromHeader.getAuthUsername())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        ApiResponse<ProductResponseDto> response = productClient.getProduct(productId);
        if (response != null && response.getData() != null && newQuantity > response.getData().getQuantity()) {
            throw new RuntimeException("Requested quantity exceeds available stock");
        }

        CartItemEntity item = cartItemsRepository
                .findByCartEntity_IdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        item.setQuantity(newQuantity);
        cartItemsRepository.save(item);

        return getCart();
    }

    @Override
    @Transactional
    public CartResponseDto getCart() {
        CartEntity cart = cartRepository.findByUsername(GetAttributesFromHeader.getAuthUsername())
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartResponseDto removeItemFromCart(long productId) {

        CartEntity cart = cartRepository.findByUsername(GetAttributesFromHeader.getAuthUsername())
                .orElseThrow(() -> new RuntimeException("Cart not found"));


        CartItemEntity item = cartItemsRepository
                .findByCartEntity_IdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        cart.getCartItems().remove(item);
        cartItemsRepository.delete(item);

        return getCart();
    }

    @Override
    @Transactional
    public void clearCart() {
        CartEntity cart = cartRepository.findByUsername(GetAttributesFromHeader.getAuthUsername())
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}