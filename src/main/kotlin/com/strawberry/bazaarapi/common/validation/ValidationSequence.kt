package com.strawberry.bazaarapi.common.validation

import javax.validation.GroupSequence
import javax.validation.groups.Default

@GroupSequence(
    ValidationOrder.First::class,
    ValidationOrder.Second::class,
    ValidationOrder.Third::class,
    ValidationOrder.Fourth::class,
    Default::class
)

interface ValidationSequence



class ValidationOrder {

    interface First

    interface Second

    interface Third

    interface Fourth
}