/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.treesync.lcs;

import junit.framework.TestCase;

import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class LCSTestCase extends TestCase {

  private static Character[] chars(String s) {
    Character[] chars = new Character[s.length()];
    for (int i = 0;i < chars.length;i++) {
      chars[i] = s.charAt(i);
    }
    return chars;
  }

  private DiffIterator<Character> diff(String s1, String s2) {
    Character[] c1 = chars(s1);
    Character[] c2 = chars(s2);
    return new LCS<Character>() {
      @Override
      protected boolean equals(Character e1, Character e2) {
        return e1.equals(e2);
      }
    }.diff(c1, c2);
  }

  public void test1() {
    DiffIterator<Character> it = diff("", "a");
    it.iterate();
    assertEquals(ChangeType.ADD, it.getType());
    assertEquals('a', (char)it.getElement());
  }

  public void test2() {
    DiffIterator<Character> it = diff("a", "");
    it.iterate();
    assertEquals(ChangeType.REMOVE, it.getType());
    assertEquals('a', (char)it.getElement());
  }

  public void test3() {
    DiffIterator<Character> it = diff("a", "a");
    it.iterate();
    assertEquals(ChangeType.KEEP, it.getType());
    assertEquals('a', (char)it.getElement());
  }

  public void test4() {
    DiffIterator<Character> it = diff("a", "b");
    it.iterate();
    assertEquals(ChangeType.ADD, it.getType());
    assertEquals('b', (char)it.getElement());
    it.iterate();
    assertEquals(ChangeType.REMOVE, it.getType());
    assertEquals('a', (char)it.getElement());
  }

  public void test5() {
    DiffIterator<Character> it = diff("", "ab");
    it.iterate();
    assertEquals(ChangeType.ADD, it.getType());
    assertEquals('b', (char)it.getElement());
    it.iterate();
    assertEquals(ChangeType.ADD, it.getType());
    assertEquals('a', (char)it.getElement());
  }

  public void test6() {
    DiffIterator<Character> it = diff("abc", "dbe");
    it.iterate();
    assertEquals(ChangeType.ADD, it.getType());
    assertEquals('e', (char)it.getElement());
    it.iterate();
    assertEquals(ChangeType.REMOVE, it.getType());
    assertEquals('c', (char)it.getElement());
    it.iterate();
    assertEquals(ChangeType.KEEP, it.getType());
    assertEquals('b', (char)it.getElement());
    it.iterate();
    assertEquals(ChangeType.ADD, it.getType());
    assertEquals('d', (char)it.getElement());
    it.iterate();
    assertEquals(ChangeType.REMOVE, it.getType());
    assertEquals('a', (char)it.getElement());
  }
}
