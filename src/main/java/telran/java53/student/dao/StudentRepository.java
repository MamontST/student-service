package telran.java53.student.dao;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import telran.java53.student.model.Student;

public interface StudentRepository extends MongoRepository<Student, Long>{
//	Student save(Student student);
//
//	// чтобы возвращать null
//	Optional<Student> findById(long id);
//
//	void deleteById(long id);
//
//	Collection<Student> findAll();
	
	Stream<Student> getAllBy();
	Stream<Student> findByNameIgnoreCase(String name);
	
	@Query("{'scores.?0': {$gt: ?1}}")
	Stream<Student> findByExamAndMinScore(String exam, Integer minScore);
	
	long countByNameInIgnoreCase(Set<String> names);
}
