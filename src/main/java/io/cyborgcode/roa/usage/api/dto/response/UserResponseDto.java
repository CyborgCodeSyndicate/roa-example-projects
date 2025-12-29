package io.cyborgcode.roa.usage.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Example response DTO from API calls.
 * <p>
 * Create response DTOs matching your API's response schemas.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private String id;
    private String name;
    private String job;
    private String createdAt;
}
