package com.strawberry.bazaarapi.category.exception

import com.strawberry.bazaarapi.common.exception.CustomNotFoundException
import com.strawberry.bazaarapi.common.exception.ExceptionMessage

class CategoryNotFoundException(exception: ExceptionMessage, notFoundId: String) :
    CustomNotFoundException(exception, notFoundId)