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

import pl.dels.model.User;

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
	private int downtime;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Activity() {

	}

	public Activity(String machineNumber, String workOrder, String side, String activityType, String comments,
			Timestamp startDateTime, Timestamp stopDateTime, int downtime) {
		this.machineNumber = machineNumber;
		this.workOrder = workOrder;
		this.side = side;
		this.activityType = activityType;
		this.comments = comments;
		this.startDateTime = startDateTime;
		this.stopDateTime = stopDateTime;
		this.downtime = downtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMachineNumber() {
		return machineNumber;
	}

	public void setMachineNumber(String machineNumber) {
		this.machineNumber = machineNumber;
	}

	public String getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(String workOrder) {
		this.workOrder = workOrder;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Timestamp getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Timestamp startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Timestamp getStopDateTime() {
		return stopDateTime;
	}

	public void setStopDateTime(Timestamp stopDateTime) {
		this.stopDateTime = stopDateTime;
	}

	public int getDowntime() {
		return downtime;
	}

	public void setDowntime(int downtime) {
		this.downtime = downtime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activityType == null) ? 0 : activityType.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + downtime;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((machineNumber == null) ? 0 : machineNumber.hashCode());
		result = prime * result + ((side == null) ? 0 : side.hashCode());
		result = prime * result + ((startDateTime == null) ? 0 : startDateTime.hashCode());
		result = prime * result + ((stopDateTime == null) ? 0 : stopDateTime.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((workOrder == null) ? 0 : workOrder.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Activity other = (Activity) obj;
		if (activityType == null) {
			if (other.activityType != null)
				return false;
		} else if (!activityType.equals(other.activityType))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (downtime != other.downtime)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (machineNumber == null) {
			if (other.machineNumber != null)
				return false;
		} else if (!machineNumber.equals(other.machineNumber))
			return false;
		if (side == null) {
			if (other.side != null)
				return false;
		} else if (!side.equals(other.side))
			return false;
		if (startDateTime == null) {
			if (other.startDateTime != null)
				return false;
		} else if (!startDateTime.equals(other.startDateTime))
			return false;
		if (stopDateTime == null) {
			if (other.stopDateTime != null)
				return false;
		} else if (!stopDateTime.equals(other.stopDateTime))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (workOrder == null) {
			if (other.workOrder != null)
				return false;
		} else if (!workOrder.equals(other.workOrder))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Activity [id=" + id + ", machineNumber=" + machineNumber + ", workOrder=" + workOrder + ", side=" + side
				+ ", activityType=" + activityType + ", comments=" + comments + ", startDateTime=" + startDateTime
				+ ", stopDateTime=" + stopDateTime + ", downtime=" + downtime + ", user=" + user + "]";
	}
}