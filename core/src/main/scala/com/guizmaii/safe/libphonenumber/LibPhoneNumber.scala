package com.guizmaii.safe.libphonenumber

import com.google.i18n.phonenumbers.NumberParseException.ErrorType
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import com.google.i18n.phonenumbers.{NumberParseException, PhoneNumberUtil}
import com.guizmaii.safe.libphonenumber.LibPhoneNumber.Show

import scala.util.control.NonFatal

sealed abstract class Country extends Product with Serializable
final case object France      extends Country

object Country {
  private[libphonenumber] final val showInstance: Show[Country] = {
    case France => "FR"
  }
}

sealed abstract class PhoneNumberParseError extends Product with Serializable
final case object INVALID_COUNTRY_CODE      extends PhoneNumberParseError
final case object NOT_A_NUMBER              extends PhoneNumberParseError
final case object TOO_SHORT_AFTER_IDD       extends PhoneNumberParseError
final case object TOO_SHORT_NSN             extends PhoneNumberParseError
final case object TOO_LONG                  extends PhoneNumberParseError
final case object UNKNOWN_ERROR             extends PhoneNumberParseError

object PhoneNumberParseError {

  def apply(googleError: ErrorType): PhoneNumberParseError =
    googleError match {
      case ErrorType.INVALID_COUNTRY_CODE => INVALID_COUNTRY_CODE
      case ErrorType.NOT_A_NUMBER         => NOT_A_NUMBER
      case ErrorType.TOO_LONG             => TOO_LONG
      case ErrorType.TOO_SHORT_AFTER_IDD  => TOO_SHORT_AFTER_IDD
      case ErrorType.TOO_SHORT_NSN        => TOO_SHORT_NSN
    }
}

object LibPhoneNumber {

  private[libphonenumber] trait Show[A] {
    def show(a: A): String
  }

  private[this] final val instance = PhoneNumberUtil.getInstance()

  final def parse(phoneNumner: String, country: Country): Either[PhoneNumberParseError, PhoneNumber] =
    try Right(instance.parse(phoneNumner, Country.showInstance.show(country)))
    catch {
      case e: NumberParseException => Left(PhoneNumberParseError(e.getErrorType))
      case NonFatal(_)             => Left(UNKNOWN_ERROR)
    }

  final def format(phoneNumber: PhoneNumber, numberFormat: PhoneNumberFormat): String =
    instance.format(phoneNumber, numberFormat)

  final def parseAndFormat(
      phoneNumber: String,
      country: Country,
      numberFormat: PhoneNumberFormat
  ): Either[PhoneNumberParseError, String] = parse(phoneNumber, country).map(format(_, numberFormat))

}
