package eu.planlos.javapretixconnector.repository;

import eu.planlos.javapretixconnector.model.PretixId;
import eu.planlos.javapretixconnector.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    Optional<ProductType> findByPretixId(PretixId pretixId);
}
