package pl.dels.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role implements Serializable {
	private static final long serialVersionUID = -8046479600118986194L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "role_name", nullable = false, unique = true)
	private String roleName;
	@Column(name = "description")
	private String roleDescription;
}
