package by.mironenko.hotelTestApp.repository.hotel;

import by.mironenko.hotelTestApp.model.Hotel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

  @Query("""
  select h from Hotel h where lower(h.name) like lower(concat('%', :name, '%')) or :name is null
  """)
  List<Hotel> findByNameContains(final String name);

  List<Hotel> findByBrand(final String brand);

  List<Hotel> findByCity(final String city);

  List<Hotel> findByCountry(final String country);

  @Query("select distinct h from Hotel h join h.amenities a where a.name in :amenities")
  List<Hotel> findByAmenities(final List<String> amenities);
}
