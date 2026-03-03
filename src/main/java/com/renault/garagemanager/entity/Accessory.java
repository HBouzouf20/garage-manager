package com.renault.garagemanager.entity;
import jakarta.persistence.*;
import lombok.*;
/**
 * Represente un accessoire associe a un vehicule.
 */
@Entity
@Table(name = "accessories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accessory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nom;
    private String description;
    @Column(nullable = false)
    private double prix;
    @Column(nullable = false)
    private String type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
}
