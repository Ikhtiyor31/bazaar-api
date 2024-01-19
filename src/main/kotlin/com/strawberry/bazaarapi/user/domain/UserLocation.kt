package com.strawberry.bazaarapi.user.domain

import javax.persistence.*

@Entity
@Table(name = "user_locations")
data class UserLocation(
    @Id
    @Column(name = "user_location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name", nullable = false)
    val name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
)
