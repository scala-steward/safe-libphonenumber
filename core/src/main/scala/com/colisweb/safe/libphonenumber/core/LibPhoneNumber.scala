package com.colisweb.safe.libphonenumber.core

import com.google.i18n.phonenumbers.NumberParseException.ErrorType
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import com.google.i18n.phonenumbers.{NumberParseException, PhoneNumberUtil}

import scala.util.control.NonFatal

sealed abstract class Country(val countryCode: String) extends Product with Serializable
object Country {
  final case object France    extends Country("FR")
  final case object Australia extends Country("AU")
}

sealed abstract class PhoneNumberError extends Product with Serializable

sealed abstract class PhoneNumberParseError extends PhoneNumberError
object PhoneNumberParseError {
  final case object InvalidCountryCode        extends PhoneNumberParseError
  final case object NotANumber                extends PhoneNumberParseError
  final case object TooShortAfterIdd          extends PhoneNumberParseError
  final case object TooShortNsn               extends PhoneNumberParseError
  final case object TooLong                   extends PhoneNumberParseError
  final case class UnknownError(e: Throwable) extends PhoneNumberParseError

  private[core] def apply(googleError: ErrorType): PhoneNumberParseError =
    googleError match {
      case ErrorType.INVALID_COUNTRY_CODE => InvalidCountryCode
      case ErrorType.NOT_A_NUMBER         => NotANumber
      case ErrorType.TOO_LONG             => TooLong
      case ErrorType.TOO_SHORT_AFTER_IDD  => TooShortAfterIdd
      case ErrorType.TOO_SHORT_NSN        => TooShortNsn
    }
}

sealed abstract class PhoneNumberFormatError extends PhoneNumberError
object PhoneNumberFormatError {
  final case object EmptyResult               extends PhoneNumberFormatError
  final case class UnknownError(e: Throwable) extends PhoneNumberFormatError
}

object LibPhoneNumber {

  private[this] final val instance = PhoneNumberUtil.getInstance()

  final def parse(phoneNumber: String, country: Country): Either[PhoneNumberParseError, PhoneNumber] =
    try Right(instance.parse(phoneNumber, country.countryCode))
    catch {
      case e: NumberParseException => Left(PhoneNumberParseError(e.getErrorType))
      case NonFatal(e)             => Left(PhoneNumberParseError.UnknownError(e))
    }

  final def format(phoneNumber: PhoneNumber, numberFormat: PhoneNumberFormat): Either[PhoneNumberFormatError, String] =
    try {
      val result: String = instance.format(phoneNumber, numberFormat)
      if (result == null || result.isBlank) Left(PhoneNumberFormatError.EmptyResult) else Right(result)
    } catch {
      case NonFatal(e) => Left(PhoneNumberFormatError.UnknownError(e))
    }

  final def parseAndFormat(
    phoneNumber: String,
    country: Country,
    numberFormat: PhoneNumberFormat
  ): Either[PhoneNumberError, String] = parse(phoneNumber, country).flatMap(format(_, numberFormat))

  final def isPossibleNumber(phoneNumber: String, country: Country): Boolean =
    parse(phoneNumber, country).map(isPossibleNumber).getOrElse(false)

  final def isPossibleNumber(phoneNumber: PhoneNumber): Boolean =
    try instance.isPossibleNumber(phoneNumber)
    catch {
      case NonFatal(_) => false
    }

}
