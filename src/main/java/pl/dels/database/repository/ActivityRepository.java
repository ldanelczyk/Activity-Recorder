package pl.dels.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.dels.model.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>{
	
	Activity findByWorkOrder (String workOrder);
}
