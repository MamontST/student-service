package telran.java53.student.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java53.student.dao.StudentRepository;
import telran.java53.student.dto.ScoreDto;
import telran.java53.student.dto.StudentAddDto;
import telran.java53.student.dto.StudentDto;
import telran.java53.student.dto.StudentUpdateDto;
import telran.java53.student.dto.exceptions.StudentNotFoundException;
import telran.java53.student.model.Student;

@Component
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
	
	final StudentRepository studentRepository;


	@Override
	public Boolean addStudent(StudentAddDto studentAddDto) {
		if (studentRepository.findById(studentAddDto.getId()).isPresent()) {
			return false;
		}
		Student student = new Student(studentAddDto.getId(), studentAddDto.getName(), studentAddDto.getPassword());
		studentRepository.save(student);
		return true;
	}

	@Override
	public StudentDto findStudent(Long id) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new StudentNotFoundException("Student with id " + id + " not found"));
		return new StudentDto(id, student.getName(), student.getScores());
	}

	@Override
	public StudentDto removeStudent(Long id) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new StudentNotFoundException("Student with id " + id + " not found"));
		studentRepository.deleteById(id);
		return new StudentDto(id, student.getName(), student.getScores());
	}

	@Override
	public StudentAddDto updateStudent(Long id, StudentUpdateDto studentUpdateDto) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new StudentNotFoundException("Student with id " + id + " not found"));
		student.setName(studentUpdateDto.getName());
		student.setPassword(studentUpdateDto.getPassword());
		studentRepository.save(student);
		return new StudentAddDto(id, studentUpdateDto.getName(), studentUpdateDto.getPassword());
	}

	@Override
	public Boolean addScore(Long id, ScoreDto scoreDto) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new StudentNotFoundException("Student with id " + id + " not found"));
		boolean res = student.addScore(scoreDto.getExamName(), scoreDto.getScore());
		studentRepository.save(student);
		return res;
	}

	@Override
	public List<StudentDto> findStudentsByName(String name) {
		return studentRepository.findByNameIgnoreCase(name)
				.map(student -> new StudentDto(student.getId(), student.getName(), student.getScores()))
				.toList();
	}

	@Override
	public Long getStudentsQuantityByNames(Set<String> names) {
		
//		return studentRepository.findAll().stream()
//				.filter(student -> names.contains(student.getName()))
//				.count();
//		return names.stream()
//				.mapToLong(name -> studentRepository.findByNameIgnoreCase(name).count())
//				.sum();
		return studentRepository.countByNameInIgnoreCase(names);
	}

	@Override
	public List<StudentDto> getStudentsByExamMinScore(String exam, Integer minScore) {
//		return studentRepository.findAll().stream()
//				.filter(s -> s.getScores().containsKey(exam) && s.getScores().get(exam) > minScore)
//				.map(s -> new StudentDto(s.getId(), s.getName(), s.getScores()))
//				.toList();
	    return studentRepository.findByExamAndMinScore(exam, minScore)
                .map(s -> new StudentDto(s.getId(), s.getName(), s.getScores()))
                .toList();
	}

}
