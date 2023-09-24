package com.strawberry.bazaarapi.common.exception

enum class ExceptionMessage(val message: String) {
    INTERNAL_SERVER_ERROR("An unknown error has occurred"),
    INVALID_USERNAME_OR_PASSWORD("the username and/or password is incorrect"),
    USERNAME_ALREADY_TAKEN("the username is already in use"),
    INVALID_AUTHORIZATION_NUMBER("invalid authorization number"),
    USER_NOT_EXIST("user doesn't exist with the email"),
    USER_ALREADY_SIGNUP("user is already exist with the email"),
    USER_NOT_SIGNUP_YET("you don't have an account yet"),
    USER_ACCESS_RESTRICTION("this feature is restricted to user member"),
    INVALID_OR_EXPIRED_USER_ACCESS_TOKEN("the access token is invalid or has expired."),
    VERIFICATION_CODE_EXPIRED("the verification code is expired."),
    EMAIL_SENDING_ERROR("the verification code is expired."),
    EMAIL_ALREADY_CONFIRMED("your email  is already confirmed!"),
    EMAIL_NOT_CONFIRMED_YET("please verify your email first"),
    USER_EMAIL_CONDITION_NOT_MET("invalid email address"),
    EMPTY_PASSWORD("please enter your password"),
    PASSWORD_MIN_LENGTH_CONDITION_NOT_MET("the password must be 6 characters or more"),
    PASSWORD_MISMATCH("the password you entered do not match")
}