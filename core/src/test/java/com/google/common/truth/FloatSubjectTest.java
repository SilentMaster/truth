/*
 * Copyright (c) 2014 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.common.truth;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import javax.annotation.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for Float Subjects.
 *
 * @author Kurt Alfred Kluever
 */
@RunWith(JUnit4.class)
public class FloatSubjectTest {
  @Test
  public void isWithinOf() {
    assertThat(2.0f).isWithin(0.0f).of(2.0f);
    assertThat(2.0f).isWithin(0.00001f).of(2.0f);
    assertThat(2.0f).isWithin(1000.0f).of(2.0f);
    assertThat(2.0f).isWithin(1.00001f).of(3.0f);
    assertThatIsWithinFails(2.0f, 0.99999f, 3.0f);
    assertThatIsWithinFails(2.0f, 1000.0f, 1003.0f);
    assertThatIsWithinFails(2.0f, 1000.0f, Float.POSITIVE_INFINITY);
    assertThatIsWithinFails(2.0f, 1000.0f, Float.NaN);
    assertThatIsWithinFails(Float.NEGATIVE_INFINITY, 1000.0f, 2.0f);
    assertThatIsWithinFails(Float.NaN, 1000.0f, 2.0f);
  }

  private static void assertThatIsWithinFails(float actual, float tolerance, float expected) {
    try {
      assertThat(actual).named("testValue").isWithin(tolerance).of(expected);
    } catch (AssertionError assertionError) {
      assertThat(assertionError)
          .hasMessage(
              String.format(
                  "testValue (<%s>) and <%s> should have been finite values within"
                      + " <%s> of each other",
                  actual, expected, tolerance));
      return;
    }
    fail("Expected AssertionError to be thrown but wasn't");
  }

  @Test
  public void isNotWithinOf() {
    assertThatIsNotWithinFails(2.0f, 0.0f, 2.0f);
    assertThatIsNotWithinFails(2.0f, 0.00001f, 2.0f);
    assertThatIsNotWithinFails(2.0f, 1000.0f, 2.0f);
    assertThatIsNotWithinFails(2.0f, 1.00001f, 3.0f);
    assertThat(2.0f).isNotWithin(0.99999f).of(3.0f);
    assertThat(2.0f).isNotWithin(1000.0f).of(1003.0f);
    assertThatIsNotWithinFails(2.0f, 0.0f, Float.POSITIVE_INFINITY);
    assertThatIsNotWithinFails(2.0f, 0.0f, Float.NaN);
    assertThatIsNotWithinFails(Float.NEGATIVE_INFINITY, 1000.0f, 2.0f);
    assertThatIsNotWithinFails(Float.NaN, 1000.0f, 2.0f);
  }

  private static void assertThatIsNotWithinFails(float actual, float tolerance, float expected) {
    try {
      assertThat(actual).named("testValue").isNotWithin(tolerance).of(expected);
    } catch (AssertionError assertionError) {
      assertThat(assertionError)
          .hasMessage(
              String.format(
                  "testValue (<%s>) and <%s> should have been finite values not within"
                      + " <%s> of each other",
                  actual, expected, tolerance));
      return;
    }
    fail("Expected AssertionError to be thrown but wasn't");
  }

  @Test
  public void negativeTolerances() {
    isWithinNegativeToleranceThrowsIAE(5.0f, -0.5f, 4.9f);
    isWithinNegativeToleranceThrowsIAE(5.0f, -0.5f, 4.0f);

    isNotWithinNegativeToleranceThrowsIAE(5.0f, -0.5f, 4.9f);
    isNotWithinNegativeToleranceThrowsIAE(5.0f, -0.5f, 4.0f);

    isWithinNegativeToleranceThrowsIAE(+0.0f, -0.00001f, +0.0f);
    isWithinNegativeToleranceThrowsIAE(+0.0f, -0.00001f, -0.0f);
    isWithinNegativeToleranceThrowsIAE(-0.0f, -0.00001f, +0.0f);
    isWithinNegativeToleranceThrowsIAE(-0.0f, -0.00001f, -0.0f);

    isNotWithinNegativeToleranceThrowsIAE(+0.0f, -0.00001f, +1.0f);
    isNotWithinNegativeToleranceThrowsIAE(+0.0f, -0.00001f, -1.0f);
    isNotWithinNegativeToleranceThrowsIAE(-0.0f, -0.00001f, +1.0f);
    isNotWithinNegativeToleranceThrowsIAE(-0.0f, -0.00001f, -1.0f);

    isNotWithinNegativeToleranceThrowsIAE(+1.0f, -0.00001f, +0.0f);
    isNotWithinNegativeToleranceThrowsIAE(+1.0f, -0.00001f, -0.0f);
    isNotWithinNegativeToleranceThrowsIAE(-1.0f, -0.00001f, +0.0f);
    isNotWithinNegativeToleranceThrowsIAE(-1.0f, -0.00001f, -0.0f);

    // You know what's worse than zero? Negative zero.

    isWithinNegativeToleranceThrowsIAE(+0.0f, -0.0f, +0.0f);
    isWithinNegativeToleranceThrowsIAE(+0.0f, -0.0f, -0.0f);
    isWithinNegativeToleranceThrowsIAE(-0.0f, -0.0f, +0.0f);
    isWithinNegativeToleranceThrowsIAE(-0.0f, -0.0f, -0.0f);

    isNotWithinNegativeToleranceThrowsIAE(+1.0f, -0.0f, +0.0f);
    isNotWithinNegativeToleranceThrowsIAE(+1.0f, -0.0f, -0.0f);
    isNotWithinNegativeToleranceThrowsIAE(-1.0f, -0.0f, +0.0f);
    isNotWithinNegativeToleranceThrowsIAE(-1.0f, -0.0f, -0.0f);
  }

  private static void isWithinNegativeToleranceThrowsIAE(
      float actual, float tolerance, float expected) {
    try {
      assertThat(actual).isWithin(tolerance).of(expected);
      fail("Expected IllegalArgumentException to be thrown but wasn't");
    } catch (IllegalArgumentException iae) {
      assertThat(iae).hasMessage("tolerance (" + tolerance + ") cannot be negative");
    }
  }

  private static void isNotWithinNegativeToleranceThrowsIAE(
      float actual, float tolerance, float expected) {
    try {
      assertThat(actual).isNotWithin(tolerance).of(expected);
      fail("Expected IllegalArgumentException to be thrown but wasn't");
    } catch (IllegalArgumentException iae) {
      assertThat(iae).hasMessage("tolerance (" + tolerance + ") cannot be negative");
    }
  }

  @Test
  public void nanTolerances() {
    try {
      assertThat(1.0f).isWithin(Float.NaN).of(1.0f);
      fail("Expected IllegalArgumentException to be thrown but wasn't");
    } catch (IllegalArgumentException iae) {
      assertThat(iae).hasMessage("tolerance cannot be NaN");
    }
    try {
      assertThat(1.0f).isNotWithin(Float.NaN).of(2.0f);
      fail("Expected IllegalArgumentException to be thrown but wasn't");
    } catch (IllegalArgumentException iae) {
      assertThat(iae).hasMessage("tolerance cannot be NaN");
    }
  }

  @Test
  public void infiniteTolerances() {
    try {
      assertThat(1.0f).isWithin(Float.POSITIVE_INFINITY).of(1.0f);
      fail("Expected IllegalArgumentException to be thrown but wasn't");
    } catch (IllegalArgumentException iae) {
      assertThat(iae).hasMessage("tolerance cannot be POSITIVE_INFINITY");
    }
    try {
      assertThat(1.0f).isNotWithin(Float.POSITIVE_INFINITY).of(2.0f);
      fail("Expected IllegalArgumentException to be thrown but wasn't");
    } catch (IllegalArgumentException iae) {
      assertThat(iae).hasMessage("tolerance cannot be POSITIVE_INFINITY");
    }
  }

  @Test
  public void isWithinOfZero() {
    assertThat(+0.0f).isWithin(0.00001f).of(+0.0f);
    assertThat(+0.0f).isWithin(0.00001f).of(-0.0f);
    assertThat(-0.0f).isWithin(0.00001f).of(+0.0f);
    assertThat(-0.0f).isWithin(0.00001f).of(-0.0f);

    assertThat(+0.0f).isWithin(0.0f).of(+0.0f);
    assertThat(+0.0f).isWithin(0.0f).of(-0.0f);
    assertThat(-0.0f).isWithin(0.0f).of(+0.0f);
    assertThat(-0.0f).isWithin(0.0f).of(-0.0f);
  }

  @Test
  public void isNotWithinOfZero() {
    assertThat(+0.0f).isNotWithin(0.00001f).of(+1.0f);
    assertThat(+0.0f).isNotWithin(0.00001f).of(-1.0f);
    assertThat(-0.0f).isNotWithin(0.00001f).of(+1.0f);
    assertThat(-0.0f).isNotWithin(0.00001f).of(-1.0f);

    assertThat(+1.0f).isNotWithin(0.00001f).of(+0.0f);
    assertThat(+1.0f).isNotWithin(0.00001f).of(-0.0f);
    assertThat(-1.0f).isNotWithin(0.00001f).of(+0.0f);
    assertThat(-1.0f).isNotWithin(0.00001f).of(-0.0f);

    assertThat(+1.0f).isNotWithin(0.0f).of(+0.0f);
    assertThat(+1.0f).isNotWithin(0.0f).of(-0.0f);
    assertThat(-1.0f).isNotWithin(0.0f).of(+0.0f);
    assertThat(-1.0f).isNotWithin(0.0f).of(-0.0f);
  }

  @Test
  public void isWithinZeroTolerance() {
    float max = Float.MAX_VALUE;
    float nearlyMax = Math.nextAfter(Float.MAX_VALUE, 0.0f);
    assertThat(max).isWithin(0.0f).of(max);
    assertThat(nearlyMax).isWithin(0.0f).of(nearlyMax);
    assertThatIsWithinFails(max, 0.0f, nearlyMax);
    assertThatIsWithinFails(nearlyMax, 0.0f, max);

    float negativeMax = -1.0f * Float.MAX_VALUE;
    float negativeNearlyMax = Math.nextAfter(-1.0f * Float.MAX_VALUE, 0.0f);
    assertThat(negativeMax).isWithin(0.0f).of(negativeMax);
    assertThat(negativeNearlyMax).isWithin(0.0f).of(negativeNearlyMax);
    assertThatIsWithinFails(negativeMax, 0.0f, negativeNearlyMax);
    assertThatIsWithinFails(negativeNearlyMax, 0.0f, negativeMax);

    float min = Float.MIN_VALUE;
    float justOverMin = Math.nextAfter(Float.MIN_VALUE, 1.0f);
    assertThat(min).isWithin(0.0f).of(min);
    assertThat(justOverMin).isWithin(0.0f).of(justOverMin);
    assertThatIsWithinFails(min, 0.0f, justOverMin);
    assertThatIsWithinFails(justOverMin, 0.0f, min);

    float negativeMin = -1.0f * Float.MIN_VALUE;
    float justUnderNegativeMin = Math.nextAfter(-1.0f * Float.MIN_VALUE, -1.0f);
    assertThat(negativeMin).isWithin(0.0f).of(negativeMin);
    assertThat(justUnderNegativeMin).isWithin(0.0f).of(justUnderNegativeMin);
    assertThatIsWithinFails(negativeMin, 0.0f, justUnderNegativeMin);
    assertThatIsWithinFails(justUnderNegativeMin, 0.0f, negativeMin);
  }

  @Test
  public void isNotWithinZeroTolerance() {
    float max = Float.MAX_VALUE;
    float nearlyMax = Math.nextAfter(Float.MAX_VALUE, 0.0f);
    assertThatIsNotWithinFails(max, 0.0f, max);
    assertThatIsNotWithinFails(nearlyMax, 0.0f, nearlyMax);
    assertThat(max).isNotWithin(0.0f).of(nearlyMax);
    assertThat(nearlyMax).isNotWithin(0.0f).of(max);

    float min = Float.MIN_VALUE;
    float justOverMin = Math.nextAfter(Float.MIN_VALUE, 1.0f);
    assertThatIsNotWithinFails(min, 0.0f, min);
    assertThatIsNotWithinFails(justOverMin, 0.0f, justOverMin);
    assertThat(min).isNotWithin(0.0f).of(justOverMin);
    assertThat(justOverMin).isNotWithin(0.0f).of(min);
  }

  @Test
  public void isWithinNonFinite() {
    assertThatIsWithinFails(Float.NaN, 0.00001f, Float.NaN);
    assertThatIsWithinFails(Float.NaN, 0.00001f, Float.POSITIVE_INFINITY);
    assertThatIsWithinFails(Float.NaN, 0.00001f, Float.NEGATIVE_INFINITY);
    assertThatIsWithinFails(Float.NaN, 0.00001f, +0.0f);
    assertThatIsWithinFails(Float.NaN, 0.00001f, -0.0f);
    assertThatIsWithinFails(Float.NaN, 0.00001f, +1.0f);
    assertThatIsWithinFails(Float.NaN, 0.00001f, -0.0f);
    assertThatIsWithinFails(Float.POSITIVE_INFINITY, 0.00001f, Float.POSITIVE_INFINITY);
    assertThatIsWithinFails(Float.POSITIVE_INFINITY, 0.00001f, Float.NEGATIVE_INFINITY);
    assertThatIsWithinFails(Float.POSITIVE_INFINITY, 0.00001f, +0.0f);
    assertThatIsWithinFails(Float.POSITIVE_INFINITY, 0.00001f, -0.0f);
    assertThatIsWithinFails(Float.POSITIVE_INFINITY, 0.00001f, +1.0f);
    assertThatIsWithinFails(Float.POSITIVE_INFINITY, 0.00001f, -0.0f);
    assertThatIsWithinFails(Float.NEGATIVE_INFINITY, 0.00001f, Float.NEGATIVE_INFINITY);
    assertThatIsWithinFails(Float.NEGATIVE_INFINITY, 0.00001f, +0.0f);
    assertThatIsWithinFails(Float.NEGATIVE_INFINITY, 0.00001f, -0.0f);
    assertThatIsWithinFails(Float.NEGATIVE_INFINITY, 0.00001f, +1.0f);
    assertThatIsWithinFails(Float.NEGATIVE_INFINITY, 0.00001f, -0.0f);
    assertThatIsWithinFails(+1.0f, 0.00001f, Float.NaN);
    assertThatIsWithinFails(+1.0f, 0.00001f, Float.POSITIVE_INFINITY);
    assertThatIsWithinFails(+1.0f, 0.00001f, Float.NEGATIVE_INFINITY);
  }

  @Test
  public void isNotWithinNonFinite() {
    assertThatIsNotWithinFails(Float.NaN, 0.00001f, Float.NaN);
    assertThatIsNotWithinFails(Float.NaN, 0.00001f, Float.POSITIVE_INFINITY);
    assertThatIsNotWithinFails(Float.NaN, 0.00001f, Float.NEGATIVE_INFINITY);
    assertThatIsNotWithinFails(Float.NaN, 0.00001f, +0.0f);
    assertThatIsNotWithinFails(Float.NaN, 0.00001f, -0.0f);
    assertThatIsNotWithinFails(Float.NaN, 0.00001f, +1.0f);
    assertThatIsNotWithinFails(Float.NaN, 0.00001f, -0.0f);
    assertThatIsNotWithinFails(Float.POSITIVE_INFINITY, 0.00001f, Float.POSITIVE_INFINITY);
    assertThatIsNotWithinFails(Float.POSITIVE_INFINITY, 0.00001f, Float.NEGATIVE_INFINITY);
    assertThatIsNotWithinFails(Float.POSITIVE_INFINITY, 0.00001f, +0.0f);
    assertThatIsNotWithinFails(Float.POSITIVE_INFINITY, 0.00001f, -0.0f);
    assertThatIsNotWithinFails(Float.POSITIVE_INFINITY, 0.00001f, +1.0f);
    assertThatIsNotWithinFails(Float.POSITIVE_INFINITY, 0.00001f, -0.0f);
    assertThatIsNotWithinFails(Float.NEGATIVE_INFINITY, 0.00001f, Float.NEGATIVE_INFINITY);
    assertThatIsNotWithinFails(Float.NEGATIVE_INFINITY, 0.00001f, +0.0f);
    assertThatIsNotWithinFails(Float.NEGATIVE_INFINITY, 0.00001f, -0.0f);
    assertThatIsNotWithinFails(Float.NEGATIVE_INFINITY, 0.00001f, +1.0f);
    assertThatIsNotWithinFails(Float.NEGATIVE_INFINITY, 0.00001f, -0.0f);
    assertThatIsNotWithinFails(+1.0f, 0.00001f, Float.NaN);
    assertThatIsNotWithinFails(+1.0f, 0.00001f, Float.POSITIVE_INFINITY);
    assertThatIsNotWithinFails(+1.0f, 0.00001f, Float.NEGATIVE_INFINITY);
  }

  @Test
  public void isPositiveInfinity() {
    assertThat(Float.POSITIVE_INFINITY).isPositiveInfinity();
    assertThatIsPositiveInfinityFails(1.23f);
    assertThatIsPositiveInfinityFails(Float.NEGATIVE_INFINITY);
    assertThatIsPositiveInfinityFails(Float.NaN);
    assertThatIsPositiveInfinityFails(null);
  }

  private static void assertThatIsPositiveInfinityFails(@Nullable Float value) {
    try {
      assertThat(value).named("testValue").isPositiveInfinity();
    } catch (AssertionError assertionError) {
      assertThat(assertionError)
          .hasMessage(
              "Not true that testValue (<"
                  + value
                  + ">) is equal to <"
                  + Float.POSITIVE_INFINITY
                  + ">");
      return;
    }
    fail("Expected AssertionError to be thrown but wasn't");
  }

  @Test
  public void isNegativeInfinity() {
    assertThat(Float.NEGATIVE_INFINITY).isNegativeInfinity();
    assertThatIsNegativeInfinityFails(1.23f);
    assertThatIsNegativeInfinityFails(Float.POSITIVE_INFINITY);
    assertThatIsNegativeInfinityFails(Float.NaN);
    assertThatIsNegativeInfinityFails(null);
  }

  private static void assertThatIsNegativeInfinityFails(@Nullable Float value) {
    try {
      assertThat(value).named("testValue").isNegativeInfinity();
    } catch (AssertionError assertionError) {
      assertThat(assertionError)
          .hasMessage(
              "Not true that testValue (<"
                  + value
                  + ">) is equal to <"
                  + Float.NEGATIVE_INFINITY
                  + ">");
      return;
    }
    fail("Expected AssertionError to be thrown but wasn't");
  }

  @Test
  public void isNaN() {
    assertThat(Float.NaN).isNaN();
    assertThatIsNaNFails(1.23f);
    assertThatIsNaNFails(Float.POSITIVE_INFINITY);
    assertThatIsNaNFails(Float.NEGATIVE_INFINITY);
    assertThatIsNaNFails(null);
  }

  private static void assertThatIsNaNFails(@Nullable Float value) {
    try {
      assertThat(value).named("testValue").isNaN();
    } catch (AssertionError assertionError) {
      assertThat(assertionError)
          .hasMessage("Not true that testValue (<" + value + ">) is equal to <" + Float.NaN + ">");
      return;
    }
    fail("Expected AssertionError to be thrown but wasn't");
  }

  @Test
  public void isFinite() {
    assertThat(1.23f).isFinite();
    assertThat(Float.MAX_VALUE).isFinite();
    assertThat(-1.0 * Float.MIN_VALUE).isFinite();
    assertThatIsFiniteFails(Float.POSITIVE_INFINITY);
    assertThatIsFiniteFails(Float.NEGATIVE_INFINITY);
    assertThatIsFiniteFails(Float.NaN);
    assertThatIsFiniteFails(null);
  }

  private static void assertThatIsFiniteFails(@Nullable Float value) {
    try {
      assertThat(value).named("testValue").isFinite();
    } catch (AssertionError assertionError) {
      assertThat(assertionError).hasMessage("testValue (<" + value + ">) should have been finite");
      return;
    }
    fail("Expected AssertionError to be thrown but wasn't");
  }

  @Test
  public void isNotNaN() {
    assertThat(1.23f).isNotNaN();
    assertThat(Float.MAX_VALUE).isNotNaN();
    assertThat(-1.0 * Float.MIN_VALUE).isNotNaN();
    assertThat(Float.POSITIVE_INFINITY).isNotNaN();
    assertThat(Float.NEGATIVE_INFINITY).isNotNaN();
    assertThatIsNotNaNFails(Float.NaN);
    assertThatIsNotNaNFails(null);
  }

  private static void assertThatIsNotNaNFails(@Nullable Float value) {
    try {
      assertThat(value).named("testValue").isNotNaN();
    } catch (AssertionError assertionError) {
      assertThat(assertionError).hasMessage("testValue (<" + value + ">) should not have been NaN");
      return;
    }
    fail("Expected AssertionError to be thrown but wasn't");
  }
}
