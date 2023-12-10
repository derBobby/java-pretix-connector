package eu.planlos.javapretixconnector.repository;

import eu.planlos.javapretixconnector.model.PretixId;
import eu.planlos.javapretixconnector.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByPretixIdAndPretixVariationId(PretixId pretixId, PretixId pretixVariationId);

    Optional<Product> findByPretixIdAndPretixVariationIdIsNull(PretixId pretixId);
}
