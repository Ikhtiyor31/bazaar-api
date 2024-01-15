package com.strawberry.bazaarapi.product.exception

import com.strawberry.bazaarapi.common.exception.CustomNotFoundException
import com.strawberry.bazaarapi.common.exception.ExceptionMessage

class ProductNotFoundException(exception: ExceptionMessage, productId: String) : CustomNotFoundException(exception, productId)
