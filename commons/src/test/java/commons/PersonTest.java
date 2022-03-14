/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

	@Test
	public void checkConstructor() {
		Person person = new Person("f", "l");
		assertNotNull(person);
	}

	@Test
	public void firstNameTest() {
		Person person = new Person("abc", "def");
		assertEquals("abc", person.firstName);
	}

	@Test
	public void lastNameTest() {
		Person person = new Person("abc", "def");
		assertEquals("def", person.lastName);
	}

	@Test
	public void getFirstNameTest() {
		Person person = new Person("abc", "def");
		assertEquals("abc", person.getFirstName());
	}

	@Test
	public void getLastNameTest() {
		Person person = new Person("abc", "def");
		assertEquals("def", person.getLastName());
	}

	@Test
	public void equalsHashCode() {
		var a = new Person("a", "b");
		var b = new Person("a", "b");
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void notEqualsHashCode() {
		var a = new Person("a", "b");
		var b = new Person("a", "c");
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void hasToString() {
		var actual = new Person("a", "b").toString();
		assertTrue(actual.contains(Person.class.getSimpleName()));
		assertTrue(actual.contains("\n"));
		assertTrue(actual.contains("firstName"));
	}
}