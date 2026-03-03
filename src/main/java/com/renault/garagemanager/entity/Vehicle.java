package com.renault.garagemanager.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Represente un vehicule stocke dans un garage.
 * Un meme modele peut exister dans plusieurs garages.
 */
@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String model;
    @Column(name = "annee_fabrication", nullable = false)
    private int anneeFabrication;
    @Column(name = "type_carburant", nullable = false)
    private String typeCarburant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Accessory> accessories = new ArrayList<>();
}
