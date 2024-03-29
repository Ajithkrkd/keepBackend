package com.ajith.secondProject.user.Response;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDetailsResponse {
    private String firstName;
    private String lastName;
    private String email;
}