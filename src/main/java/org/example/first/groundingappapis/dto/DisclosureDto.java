package org.example.first.groundingappapis.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DisclosureDto {

    private UUID id;
    private String title;
    private String content;
    private LocalDate reportedAt;

    @Builder
    public DisclosureDto(UUID id, String title, String content, LocalDate reportedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.reportedAt = reportedAt;
    }

}
