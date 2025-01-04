package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pack.model.Jikwon;

@Repository
public interface JikwonRepository extends JpaRepository<Jikwon, Long>{

}
