package pl.dels.model;

import java.io.Serializable;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import pl.dels.model.User;

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
public class Activity implements Serializable {
	private static final long serialVersionUID = -8046479600118986194L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(nullable = false, name = "machine_number")
	private String machineNumber;
	@Column(nullable = false, name = "work_order")
	private String workOrder;
	@Column(nullable = false)
	private String side;
	@Column(nullable = false, name = "activity_type")
	private String activityType;
	@Column
	private String comments;
	@Column(nullable = false, name = "start_date_time")
	private Timestamp startDateTime;
	@Column(nullable = false, name = "stop_date_time")
	private Timestamp stopDateTime;
	@Column(nullable = false)
	private double downtime;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}