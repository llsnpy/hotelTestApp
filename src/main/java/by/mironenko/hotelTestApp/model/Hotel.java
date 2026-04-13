package by.mironenko.hotelTestApp.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hotel")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(length = 2000)
  private String description;

  @Column(nullable = false)
  private String brand;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "houseNumber", column = @Column(name = "house_number")),
      @AttributeOverride(name = "street", column = @Column(name = "street")),
      @AttributeOverride(name = "city", column = @Column(name = "city")),
      @AttributeOverride(name = "country", column = @Column(name = "country")),
      @AttributeOverride(name = "postCode", column = @Column(name = "post_code")),
  })
  private Address address;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "phone", column = @Column(name = "phone")),
      @AttributeOverride(name = "email", column = @Column(name = "email"))
  })
  private Contact contact;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "checkIn", column = @Column(name = "check_in")),
      @AttributeOverride(name = "checkOut", column = @Column(name = "check_out")),
  })
  private ArrivalTime arrivalTime;

  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  @JoinTable(
      name = "hotel_amenity",
      joinColumns = @JoinColumn(name = "hotel_id"),
      inverseJoinColumns = @JoinColumn(name = "amenity_id")
  )
  @Builder.Default
  private Set<Amenity> amenities = new HashSet<>();
}
