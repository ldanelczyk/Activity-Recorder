package pl.dels.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {
	private static final long serialVersionUID = -8046479600118986194L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(nullable = false, unique = true)
	private String username;
	@Column(nullable = false)
	private String password;
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private Set<Role> roles = new HashSet<>();
	@OneToMany(mappedBy = "user")
	private List<Activity> activities;

	public User() {

	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
}