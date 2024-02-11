package infrastructure.dto.in;


import lombok.Data;

@Data
public class StarredListingsDto {
    private String userId; //
    private Integer page;  // currentPage request
    private Integer size;  // Page size request

}
