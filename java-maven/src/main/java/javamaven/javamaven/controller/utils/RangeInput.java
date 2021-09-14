package javamaven.javamaven.controller.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RangeInput {
    private final String startDate;
    private final String endDate;
}
