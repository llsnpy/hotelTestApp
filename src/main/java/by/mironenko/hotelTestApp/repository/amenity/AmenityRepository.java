package by.mironenko.hotelTestApp.repository.amenity;

import by.mironenko.hotelTestApp.model.Amenity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {

  Optional<Amenity> findByName(final String name);
}
