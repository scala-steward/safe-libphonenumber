package com.guizmaii.safe.libphonenumber.core

import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class LibPhoneNumberTest extends AnyFlatSpec with Matchers {

  behavior of "LibPhoneNumberTest"

  it should "format" in {}

  it should "parse" in {}

  it should "parseAndFormat" in {
    LibPhoneNumber.parseAndFormat("3369678876", Country.France, PhoneNumberFormat.E164) shouldBe Right("+3369678876")
  }

}
