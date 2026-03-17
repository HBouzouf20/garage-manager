package com.renault.garagemanager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.DayOfWeek;

/**
 * JPA entity representing an opening time slot for a garage on a given day of the week.
 */
@Entity
@Table(name = "opening_hours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningHourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Embedded
    private OpeningTimeEntity openingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    private GarageEntity garage;
}
