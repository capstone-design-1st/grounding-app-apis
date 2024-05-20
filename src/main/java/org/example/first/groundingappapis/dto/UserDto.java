package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {

    private UUID id;
    private String email;
    private String password;
    private String phoneNumber;
    private String nickname;

    @Builder
    public UserDto(
                    UUID id,
                    String email,
                   String password,
                   String phoneNumber,
                   String nickname) {
        this.id = id;
        this.email = email != null ? email : "";
        this.password = password != null ? password : "";
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
        this.nickname = nickname != null ? nickname : "";
    }


    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetResponse{}
    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadResponse{}
}
