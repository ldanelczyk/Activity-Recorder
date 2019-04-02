package pl.dels.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.dels.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long>{
	
	Activity findByWorkOrder (String workOrder);
}
