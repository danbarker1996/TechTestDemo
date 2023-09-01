package uk.co.gamma.address.model;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

public record Address(

        @Null
        Integer id,

        @Schema(description = "Name of the building", example = "King's House")
        @NotEmpty(message = "Building must not be empty")
        String building,

        @Schema(description = "Name of the street", example = "Kings Road West")
        @NotEmpty(message = "Street must not be empty")
        String street,

        @Schema(description = "Name of the town", example = "Newbury")
        @NotEmpty(message = "Town must not be empty")
        String town,

        @Schema(description = "Postcode for the address", example = "RG14 5BY")
        @NotEmpty(message = "Postcode must not be empty")
        String postcode
) {
}