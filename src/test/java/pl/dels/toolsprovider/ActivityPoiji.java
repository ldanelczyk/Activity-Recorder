package pl.dels.toolsprovider;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author danelczykl
 *
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "activities")
public class ActivityPoiji implements Serializable {
	private static final long serialVersionUID = -8046479600118986194L;

	@ExcelRow
	private int rowIndex;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@ExcelCell(0)
	@Column(nullable = false, name = "machine_number")
	private String machineNumber;
	@ExcelCell(1)
	@Column(nullable = false, name = "work_order")
	private String workOrder;
	@ExcelCell(2)
	@Column(nullable = false)
	private String side;
	@ExcelCell(3)
	@Column(nullable = false, name = "activity_type")
	private String activityType;
	@ExcelCell(4)
	@Column
	private String comments;
	@ExcelCell(5)
	@Column(nullable = false, name = "start_date_time")
	private Date startDateTime;
	@ExcelCell(6)
	@Column(nullable = false, name = "stop_date_time")
	private Date stopDateTime;
	@ExcelCell(7)
	@Column(nullable = false)
	private double downtime;
	@ExcelCell(8)
	@ManyToOne
	@JoinColumn(name = "user_id")
	private String user;
}