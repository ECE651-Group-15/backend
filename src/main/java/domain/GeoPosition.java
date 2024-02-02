package domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GeoPosition {
    double latitude;
    double longitude;
}
