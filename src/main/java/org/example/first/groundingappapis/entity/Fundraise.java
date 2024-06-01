package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.FundraiseDto;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "fundraises")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Fundraise {
    @Id
    @Column(name = "fundraise_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)",foreignKey = @ForeignKey(name = "fk_fundraise_property"))
    private Property property;

    //진행률
    @Column(name = "progress_rate", columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double progressRate;

    @Column(name = "progress_amount", columnDefinition = "BIGINT DEFAULT 0")
    private Integer progressAmount;

    //투자 참여 인원
    @Column(name = "investor_count", columnDefinition = "INT DEFAULT 0")
    private Integer investorCount;

    //증권 종류
    @Column(name = "security_type", columnDefinition = "VARCHAR(20) DEFAULT '주식'")
    private String securityType;

    //발행인
    @Column(name = "issuer", columnDefinition = "VARCHAR(20) DEFAULT '주식회사'")
    private String issuer;

    //발행 증권 수
    @Column(name = "security_count", columnDefinition = "INT DEFAULT 0")
    private Integer securityCount;

    //발행 가액
    @Column(name = "issue_price", columnDefinition = "INT DEFAULT 0")
    private Integer issuePrice;

    //총 모집액
    @Column(name = "total_fund", columnDefinition = "BIGINT DEFAULT 0")
    private Integer totalFund;

    //청약 시작일
    @Column(name = "subscription_start_date", columnDefinition = "DATE")
    private LocalDate subscriptionStartDate;

    //청약 마감일
    @Column(name = "subscription_end_date", columnDefinition = "DATE")
    private LocalDate subscriptionEndDate;

    //공간운영사정보
    @Column(name = "operator_name", columnDefinition = "VARCHAR(20)")
    private String operatorName;

    //회사소개
    @Lob
    @Column(name = "operator_introduction")
    private String operatorIntroduction;

    @Builder
    public Fundraise(Property property,
                     Double progressRate,
                     Integer investorCount,
                     String securityType,
                     String issuer,
                     Integer securityCount,
                     Integer issuePrice,
                     Integer totalFund,
                     LocalDate subscriptionStartDate,
                     LocalDate subscriptionEndDate,
                     String operatorName,
                     String operatorIntroduction) {

        this.property = property;
        this.progressAmount = progressAmount;
        this.progressRate = progressRate;
        this.investorCount = investorCount;
        this.securityType = securityType;
        this.issuer = issuer;
        this.securityCount = securityCount;
        this.issuePrice = issuePrice;
        this.totalFund = totalFund;
        this.subscriptionStartDate = subscriptionStartDate;
        this.subscriptionEndDate = subscriptionEndDate;
        this.operatorName = operatorName;
        this.operatorIntroduction = operatorIntroduction;
    }

    public FundraiseDto toDto() {
        return FundraiseDto.builder()
                .progressRate(progressRate)
                .progressAmount(progressAmount)
                .investorCount(investorCount)
                .securityType(securityType)
                .issuer(issuer)
                .securityCount(securityCount)
                .issuePrice(issuePrice)
                .totalFund(totalFund)
                .subscriptionStartDate(subscriptionStartDate)
                .subscriptionEndDate(subscriptionEndDate)
                .operatorName(operatorName)
                .operatorIntroduction(operatorIntroduction)
                .build();
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.setFundraise(this);
    }

    public void setProgress(int progressAmount) {
        this.progressAmount = progressAmount;
        this.progressRate = (double) progressAmount / totalFund * 100;
    }

    public void setSubscriptionEndDate(LocalDate now) {
        this.subscriptionEndDate = now;
    }

    public void increaseInvestorCount() {
        this.investorCount++;
    }
}
