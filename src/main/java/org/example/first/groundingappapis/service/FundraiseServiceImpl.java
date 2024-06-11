package org.example.first.groundingappapis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.FundraiseDto;
import org.example.first.groundingappapis.entity.*;
import org.example.first.groundingappapis.exception.*;
import org.example.first.groundingappapis.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FundraiseServiceImpl implements FundraiseService{
    private final PropertyRepository propertyRepository;
    private final OrderRepository orderRepository;
    private final RealTimeTransactionLogRepository realTimeTransactionLogRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final FundraiseRepository fundraiseRepository;
    private final DayTransactionLogRepository dayTransactionLogRepository;
    private void validateFundraiseOrder(Fundraise fundraise, Account buyerAccount, User buyer, Property property, Long totalSubscriptionPrice) {
        if (fundraise == null) {
            throw new PropertyException(PropertyErrorResult.FUNDRAISE_NOT_FOUND);
        }
        if(orderRepository.existsByUserAndPropertyAndType(buyer, property, "체결 완료")){
            throw new TradingException(TradingErrorResult.ALREADY_SUBSCRIBED);
        }
        if(buyerAccount.getDeposit() < totalSubscriptionPrice){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_DEPOSIT);
        }
        if (fundraise.getSubscriptionEndDate().isBefore(LocalDate.now())) {
            throw new TradingException(TradingErrorResult.FUNDRAISE_ENDED);
        }
        if (fundraise.getTotalFund() < fundraise.getProgressAmount() + totalSubscriptionPrice) {
            throw new TradingException(TradingErrorResult.EXCEED_FUNDRAISE_TOTAL_AMOUNT);
        }

    }

    @Transactional
    @Override
    public FundraiseDto.FundraiseResponse fundraiseProperty(String propertyId, FundraiseDto.FundraiseRequest fundraiseRequest, UUID userId) {
        final User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        final Account buyerAccount = buyer.getAccount();
        log.info("fundraiseProperty called with propertyId: {} and account: {}", propertyId, buyerAccount.getId());

        final Property property = propertyRepository.findById(UUID.fromString(propertyId))
                .orElseThrow(() -> new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));

        final Fundraise fundraise = property.getFundraise();

        Long totalSubscriptionPrice = Long.valueOf(fundraise.getIssuePrice() * fundraiseRequest.getQuantity());

        validateFundraiseOrder(fundraise, buyerAccount, buyer, property, totalSubscriptionPrice);

        Order newOrder = Order.builder()
                .type("매수")
                .quantity(Long.valueOf(fundraiseRequest.getQuantity()))
                .price(fundraise.getIssuePrice())
                .status("청약중")
                .build();
        newOrder.updateUser(buyer);
        newOrder.updateProperty(property);

        orderRepository.save(newOrder);

        RealTimeTransactionLog realTimeTransactionLog = RealTimeTransactionLog.builder()
                .executedPrice(fundraise.getIssuePrice())
                .quantity(fundraiseRequest.getQuantity())
                .executedAt(LocalDateTime.now())
                .fluctuationRate(0.0)
                .build();

        realTimeTransactionLog.updateProperty(property);

        realTimeTransactionLogRepository.save(realTimeTransactionLog);

        buyerAccount.minusDeposit(totalSubscriptionPrice);

        Inventory inventory = Inventory.builder()
                .quantity(fundraiseRequest.getQuantity())
                .sellableQuantity(fundraiseRequest.getQuantity())
                .earningsRate(0.0)
                .averageBuyingPrice(fundraise.getIssuePrice())
                .build();

        inventory.updateProperty(property);
        inventory.updateAccount(buyerAccount);

        inventoryRepository.save(inventory);

        Long currentProgressAmount = fundraise.getProgressAmount();
        fundraise.setProgress(Long.valueOf(currentProgressAmount + totalSubscriptionPrice));
        fundraise.increaseInvestorCount();
        fundraiseRepository.save(fundraise);

        if(fundraise.getTotalFund().equals(fundraise.getProgressAmount())){
            fundraise.setSubscriptionEndDate(LocalDate.now());
            fundraiseRepository.save(fundraise);

            DayTransactionLog dayTransactionLog = DayTransactionLog.builder()
                    .closingPrice(fundraise.getIssuePrice())
                    .openingPrice(fundraise.getIssuePrice())
                    .closingPrice(fundraise.getIssuePrice())
                    .maxPrice(fundraise.getIssuePrice())
                    .minPrice(fundraise.getIssuePrice())
                    .fluctuationRate(0.0)
                    .volumeCount(Long.valueOf(fundraise.getSecurityCount()))
                    .date(LocalDate.now())
                    .build();

            dayTransactionLog.updateProperty(property);
            dayTransactionLogRepository.save(dayTransactionLog);

            List<Order> orders = orderRepository.findByStatusAndProperty("청약중", property);
            for(Order order : orders){
                order.updateStatus("체결 완료");
                orderRepository.save(order);
            }
        }

        property.increaseTotalVolume(fundraiseRequest.getQuantity());
        propertyRepository.save(property);

        return FundraiseDto.FundraiseResponse.builder()
                .userId(buyer.getId().toString())
                .propertyId(propertyId)
                .quantity(fundraiseRequest.getQuantity())
                .price(fundraise.getIssuePrice())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
