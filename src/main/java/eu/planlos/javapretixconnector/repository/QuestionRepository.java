package eu.planlos.javapretixconnector.repository;

import eu.planlos.javapretixconnector.model.PretixId;
import eu.planlos.javapretixconnector.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByPretixId(PretixId questionId);
}
