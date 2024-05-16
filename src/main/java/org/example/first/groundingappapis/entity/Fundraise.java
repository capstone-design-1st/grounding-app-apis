package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "fundraises")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Fundraise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fundraise_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, foreignKey = @ForeignKey(name = "fk_fundraise_property"))
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

    @Builder
    public Fundraise(Property property, Double progressRate, LocalDate deadline, Integer investorCount) {
        this.property = property;
        this.progressRate = progressRate;
        this.deadline = deadline;
        this.investorCount = investorCount;
    }
}
