package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.dto.RealTimeTransactionLogDto;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.RealTimeTransactionLog;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealTimeTransactionLogRepository extends JpaRepository<RealTimeTransactionLog, Long> {

    /*        final Pageable pageable = PageRequest.of(page, size);

        final User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        //먼저, 유저가 좋아요한 매물을 페이징 조회한다.
        final Page<Property> userLikedProperty = likeRepository.findAllLikedPropertyByUser(user, pageable);

        //제일 최근에 executed 된 transaction을 조회한다.
        //이때, property와 user가 일치하는 transaction을 조회한다.
        RealTimeTransactionLogDto realTimeTransactionLogDto = realTimeTransactionLogRepository.findFirstByPropertyAndUserOrderByExecutedAtDesc(userLikedProperty, user);


     */
//    @Query("SELECT new org.example.first.groundingappapis.dto.RealTimeTransactionLogDto " +
//            "(r.executedAt, r.quantity, r.executedPrice, r.fluctuationRate) " +
//            "FROM RealTimeTransactionLog r WHERE r.property IN :userLikedProperty AND r.user = :user ORDER BY r.executedAt DESC")

    @Query("SELECT new org.example.first.groundingappapis.dto.RealTimeTransactionLogDto(" +
            "r.property.id, r.executedAt, r.quantity, r.executedPrice, r.fluctuationRate) " +
            "FROM RealTimeTransactionLog r " +
            "WHERE r.property IN :properties AND r.user = :user " +
            "ORDER BY r.executedAt DESC")
    List<RealTimeTransactionLogDto> findRecentTransactionLogsByUserAndProperties(@Param("user") User user, @Param("properties") List<Property> properties);

}
