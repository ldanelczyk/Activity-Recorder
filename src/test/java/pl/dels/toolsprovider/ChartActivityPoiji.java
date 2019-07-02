package pl.dels.toolsprovider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChartActivityPoiji {
	
	private String workOrder;
	
	private double downtime;
}
