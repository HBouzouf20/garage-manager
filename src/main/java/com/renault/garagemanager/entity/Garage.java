package com.renault.garagemanager.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Represente un garage affilie au reseau Renault.
 * Chaque garage peut stocker au maximum 50 vehicules.
 */
@Entity
@Table(name = "garages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String telephone;
    @Column(nullable = false)
    private String email;
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OpeningHour> horairesOuverture = new ArrayList<>();
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Vehicle> vehicles = new ArrayList<>();
}
