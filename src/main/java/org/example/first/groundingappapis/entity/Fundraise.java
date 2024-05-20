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
    @Column(name = "progress_rate")
    private Double progressRate;

    //마감일
    @Column(name = "deadline")
    private LocalDate deadline;

    //투자 참여 인원
    @Column(name = "investor_count")
    private Integer investorCount;

    //증권 종류
    @Column(name = "security_type", length = 20)
    private String securityType;

    //발행인
    @Column(name = "issuer", length = 20)
    private String issuer;

    //발행 증권 수
    @Column(name = "security_count")
    private Integer securityCount;

    //발행 가액
    @Column(name = "issue_price")
    private Long issuePrice;

    //총 모집액
    @Column(name = "total_fund")
    private Integer totalFund;

    //청약 시작일
    @Column(name = "subscription_start_date")
    private LocalDate subscriptionStartDate;

    //청약 마감일
    @Column(name = "subscription_end_date")
    private LocalDate subscriptionEndDate;

    //공간운영사정보
    @Column(name = "operator_name")
    private String operatorName;

    //회사소개
    @Column(name = "operator_introduction")
    private String operatorIntroduction;

    @Builder
    public Fundraise(Property property,
                     Double progressRate,
                     LocalDate deadline,
                     Integer investorCount,
                     String securityType,
                     String issuer,
                     Integer securityCount,
                     Long issuePrice,
                     Integer totalFund,
                     LocalDate subscriptionStartDate,
                     LocalDate subscriptionEndDate,
                     String operatorName,
                     String operatorIntroduction) {

        this.property = property;
        this.progressRate = progressRate != null ? progressRate : 0.0;
        this.deadline = deadline != null ? deadline : LocalDate.now();
        this.investorCount = investorCount != null ? investorCount : 0;
        this.securityType = securityType != null ? securityType : "";
        this.issuer = issuer != null ? issuer : "";
        this.securityCount = securityCount != null ? securityCount : 0;
        this.issuePrice = issuePrice != null ? issuePrice : 0;
        this.totalFund = totalFund != null ? totalFund : 0;
        this.subscriptionStartDate = subscriptionStartDate != null ? subscriptionStartDate : LocalDate.now();
        this.subscriptionEndDate = subscriptionEndDate != null ? subscriptionEndDate : LocalDate.now();
        this.operatorName = operatorName != null ? operatorName : "";
        this.operatorIntroduction = operatorIntroduction != null ? operatorIntroduction : "";
    }

    public FundraiseDto toDto() {
        return FundraiseDto.builder()
                .progressRate(progressRate)
                .deadline(deadline)
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
}
