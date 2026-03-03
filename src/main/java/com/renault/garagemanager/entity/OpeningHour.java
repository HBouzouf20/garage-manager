package com.renault.garagemanager.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.DayOfWeek;
/**
 * Creneau horaire d'ouverture d'un garage pour un jour donne.
 */
@Entity
@Table(name = "opening_hours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;
    @Embedded
    private OpeningTime openingTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;
}
