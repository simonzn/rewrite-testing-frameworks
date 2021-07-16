/*
 * Copyright 2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.java.testing.assertj

import org.junit.jupiter.api.Test
import org.openrewrite.Parser
import org.openrewrite.Recipe
import org.openrewrite.java.JavaParser
import org.openrewrite.java.JavaRecipeTest
import org.openrewrite.java.tree.J

class JUnitAssertEqualsToAssertThatTest : JavaRecipeTest {
    override val parser: JavaParser = JavaParser.fromJavaVersion()
        .classpath("junit")
        .build()

    override val recipe: Recipe
        get() = JUnitAssertEqualsToAssertThat()

    @Test
    fun singleStaticMethodNoMessage() = assertChanged(
        before = """
            import org.junit.Test;

            import static org.junit.jupiter.api.Assertions.assertEquals;

            public class A {

                @Test
                public void test() {
                    assertEquals(1, notification());
                }
                private Integer notification() {
                    return 1;
                }
            }
        """,
        after = """
            import org.junit.Test;

            import static org.assertj.core.api.Assertions.assertThat;

            public class A {

                @Test
                public void test() {
                    assertThat(notification()).isEqualTo(1);
                }
                private Integer notification() {
                    return 1;
                }
            }
        """
    )

    @Test
    fun singleStaticMethodWithMessage() = assertChanged(
        before = """
            import org.junit.Test;

            import static org.junit.jupiter.api.Assertions.assertEquals;

            public class A {

                @Test
                public void test() {
                    assertEquals("fred", notification(), () -> "These should be equal");
                }
                private String notification() {
                    return "fred";
                }
            }
        """,
        after = """
            import org.junit.Test;

            import static org.assertj.core.api.Assertions.assertThat;

            public class A {

                @Test
                public void test() {
                    assertThat(notification()).withFailMessage(() -> "These should be equal").isEqualTo("fred");
                }
                private String notification() {
                    return "fred";
                }
            }
        """
    )

    @Test
    fun doubleCloseToWithNoMessage() = assertChanged(
        before = """
            import org.junit.Test;

            import static org.junit.jupiter.api.Assertions.assertEquals;

            public class A {

                @Test
                public void test() {
                    assertEquals(0.0d, notification(), 0.2d);
                }
                private Double notification() {
                    return 0.1d;
                }
            }
        """,
        after = """
            import org.junit.Test;

            import static org.assertj.core.api.Assertions.assertThat;
            import static org.assertj.core.api.Assertions.within;

            public class A {

                @Test
                public void test() {
                    assertThat(notification()).isCloseTo(0.0d, within(0.2d));
                }
                private Double notification() {
                    return 0.1d;
                }
            }
        """
    )

    @Test
    fun doubleCloseToWithMessage() = assertChanged(
        before = """
            import org.junit.Test;

            import static org.junit.jupiter.api.Assertions.assertEquals;

            public class A {

                @Test
                public void test() {
                    assertEquals(0.0d, notification(), 0.2d, "These should be close.");
                }
                private double notification() {
                    return 0.1d;
                }
            }
        """,
        after = """
            import org.junit.Test;

            import static org.assertj.core.api.Assertions.assertThat;
            import static org.assertj.core.api.Assertions.within;

            public class A {

                @Test
                public void test() {
                    assertThat(notification()).as("These should be close.").isCloseTo(0.0d, within(0.2d));
                }
                private double notification() {
                    return 0.1d;
                }
            }
        """,
        skipEnhancedTypeValidation = true
    )

    @Test
    fun doubleObjectsCloseToWithMessage() = assertChanged(
        before = """
            import org.junit.Test;

            import static org.junit.jupiter.api.Assertions.assertEquals;

            public class A {

                @Test
                public void test() {
                    assertEquals(Double.valueOf(0.0d), notification(), Double.valueOf(0.2d), () -> "These should be close.");
                }
                private double notification() {
                    return Double.valueOf(0.1d);
                }
            }
        """,
        after = """
            import org.junit.Test;

            import static org.assertj.core.api.Assertions.assertThat;
            import static org.assertj.core.api.Assertions.within;

            public class A {

                @Test
                public void test() {
                    assertThat(notification()).withFailMessage(() -> "These should be close.").isCloseTo(Double.valueOf(0.0d), within(Double.valueOf(0.2d)));
                }
                private double notification() {
                    return Double.valueOf(0.1d);
                }
            }
        """,
        skipEnhancedTypeValidation = true
    )

    @Test
    fun floatCloseToWithNoMessage() = assertChanged(
        before = """
            import org.junit.Test;

            import static org.junit.jupiter.api.Assertions.assertEquals;

            public class A {

                @Test
                public void test() {
                    assertEquals(0.0f, notification(), 0.2f);
                }
                private Float notification() {
                    return 0.1f;
                }
            }
        """,
        after = """
            import org.junit.Test;

            import static org.assertj.core.api.Assertions.assertThat;
            import static org.assertj.core.api.Assertions.within;

            public class A {

                @Test
                public void test() {
                    assertThat(notification()).isCloseTo(0.0f, within(0.2f));
                }
                private Float notification() {
                    return 0.1f;
                }
            }
        """
    )

    @Test
    fun floatCloseToWithMessage() = assertChanged(
        before = """
            import org.junit.Test;

            import static org.junit.jupiter.api.Assertions.assertEquals;

            public class A {

                @Test
                public void test() {
                    assertEquals(0.0f, notification(), 0.2f, "These should be close.");
                }
                private float notification() {
                    return 0.1f;
                }
            }
        """,
        after = """
            import org.junit.Test;

            import static org.assertj.core.api.Assertions.assertThat;
            import static org.assertj.core.api.Assertions.within;

            public class A {

                @Test
                public void test() {
                    assertThat(notification()).as("These should be close.").isCloseTo(0.0f, within(0.2f));
                }
                private float notification() {
                    return 0.1f;
                }
            }
        """,
        skipEnhancedTypeValidation = true
    )

    @Test
    fun fullyQualifiedMethodWithMessage() = assertChanged(
        before = """
            import org.junit.Test;
            import java.io.File;

            public class A {

                @Test
                public void test() {
                    org.junit.jupiter.api.Assertions.assertEquals(new File("someFile"), notification(), "These should be equal");
                }
                private File notification() {
                    return new File("someFile");
                }
            }
        """,
        after = """
            import org.junit.Test;
            import java.io.File;

            import static org.assertj.core.api.Assertions.assertThat;

            public class A {

                @Test
                public void test() {
                    assertThat(notification()).as("These should be equal").isEqualTo(new File("someFile"));
                }
                private File notification() {
                    return new File("someFile");
                }
            }
        """,
        skipEnhancedTypeValidation = true
    )
}
