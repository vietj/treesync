/*
 * Copyright (C) 2011 eXo Platform SAS.
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

package org.exoplatform.diff.list;

import junit.framework.TestCase;
import org.exoplatform.diff.JavaUtilListAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ListDiffTestCase extends TestCase {

   private static List<Character> chars(String s) {
      Character[] chars = new Character[s.length()];
      for (int i = 0; i < chars.length; i++) {
         chars[i] = s.charAt(i);
      }
      return Arrays.asList(chars);
   }

   private ListChangeIterator<List<Character>, List<Character>, Character> diff(String s1, String s2) {
      List<Character> c1 = chars(s1);
      List<Character> c2 = chars(s2);
      return new ListDiff<List<Character>, List<Character>, Character>(new JavaUtilListAdapter<Character>(), new JavaUtilListAdapter<Character>()).iterator(c1, c2);
   }

   public void test0() {
      ListChangeIterator<List<Character>, List<Character>, Character> it = diff("", "");
      assertEquals(0, it.getIndex1());
      assertEquals(0, it.getIndex2());
      assertFalse(it.hasNext());
      assertEquals("", it.getMatrix());
   }

   public void test1() {
      ListChangeIterator<List<Character>, List<Character>, Character> it = diff("", "a");
      assertEquals(0, it.getIndex1());
      assertEquals(0, it.getIndex2());
      assertEquals(ListChangeType.ADD, it.next());
      assertEquals('a', (char) it.getElement());
      assertEquals(0, it.getIndex1());
      assertEquals(1, it.getIndex2());
      assertFalse(it.hasNext());
      assertEquals("", it.getMatrix());
   }

   public void test2() {
      ListChangeIterator<List<Character>, List<Character>, Character> it = diff("a", "");
      assertEquals(0, it.getIndex1());
      assertEquals(0, it.getIndex2());
      assertEquals(ListChangeType.REMOVE, it.next());
      assertEquals('a', (char) it.getElement());
      assertEquals(1, it.getIndex1());
      assertEquals(0, it.getIndex2());
      assertFalse(it.hasNext());
      assertEquals("", it.getMatrix());
   }

   public void test3() {
      ListChangeIterator<List<Character>, List<Character>, Character> it = diff("a", "a");
      assertEquals(0, it.getIndex1());
      assertEquals(0, it.getIndex2());
      assertEquals(ListChangeType.SAME, it.next());
      assertEquals('a', (char) it.getElement());
      assertEquals(1, it.getIndex1());
      assertEquals(1, it.getIndex2());
      assertFalse(it.hasNext());
      assertEquals("", it.getMatrix());
   }

   public void test4() {
      ListChangeIterator<List<Character>, List<Character>, Character> it = diff("a", "b");
      assertEquals(0, it.getIndex1());
      assertEquals(0, it.getIndex2());
      assertEquals(ListChangeType.ADD, it.next());
      assertEquals('b', (char) it.getElement());
      assertEquals(0, it.getIndex1());
      assertEquals(1, it.getIndex2());
      assertEquals(ListChangeType.REMOVE, it.next());
      assertEquals('a', (char) it.getElement());
      assertEquals(1, it.getIndex1());
      assertEquals(1, it.getIndex2());
      assertFalse(it.hasNext());
      assertFalse(it.getMatrix().equals(""));
   }

   public void test5() {
      ListChangeIterator<List<Character>, List<Character>, Character> it = diff("", "ab");
      assertEquals(0, it.getIndex1());
      assertEquals(0, it.getIndex2());
      assertEquals(ListChangeType.ADD, it.next());
      assertEquals('a', (char) it.getElement());
      assertEquals(0, it.getIndex1());
      assertEquals(1, it.getIndex2());
      assertEquals(ListChangeType.ADD, it.next());
      assertEquals('b', (char) it.getElement());
      assertEquals(0, it.getIndex1());
      assertEquals(2, it.getIndex2());
      assertFalse(it.hasNext());
      assertEquals("", it.getMatrix());
   }

   public void test6() {
      ListChangeIterator<List<Character>, List<Character>, Character> it = diff("abc", "dbe");
      assertEquals(0, it.getIndex1());
      assertEquals(0, it.getIndex2());
      assertEquals(ListChangeType.ADD, it.next());
      assertEquals('d', (char) it.getElement());
      assertEquals(0, it.getIndex1());
      assertEquals(1, it.getIndex2());
      assertEquals(ListChangeType.REMOVE, it.next());
      assertEquals('a', (char) it.getElement());
      assertEquals(1, it.getIndex1());
      assertEquals(1, it.getIndex2());
      assertEquals(ListChangeType.SAME, it.next());
      assertEquals('b', (char) it.getElement());
      assertEquals(2, it.getIndex1());
      assertEquals(2, it.getIndex2());
      assertEquals(ListChangeType.ADD, it.next());
      assertEquals(2, it.getIndex1());
      assertEquals(3, it.getIndex2());
      assertEquals('e', (char) it.getElement());
      assertEquals(ListChangeType.REMOVE, it.next());
      assertEquals('c', (char) it.getElement());
      assertEquals(3, it.getIndex1());
      assertEquals(3, it.getIndex2());
      assertFalse(it.hasNext());
      assertFalse(it.getMatrix().equals(""));
   }

   // See http://en.wikipedia.org/wiki/Longest_common_subsequence_problem
   public void testWikipedia() {
      List<Character> c1 = chars("UXWAJZM");
      List<Character> c2 = chars("ZUAYJMX");
      ListDiff<List<Character>, List<Character>, Character> seq = new ListDiff<List<Character>, List<Character>, Character>(new JavaUtilListAdapter<Character>(), new JavaUtilListAdapter<Character>());
      ListChangeIterator i = seq.iterator(c1, c2);

      // Force a load because it's lazy
      i.hasNext();
      String s =
            "[0,0,0,0,0,0,0,0]\n" +
            "[0,0,1,1,1,1,1,1]\n" +
            "[0,0,1,1,1,1,1,2]\n" +
            "[0,0,1,2,2,2,2,2]\n" +
            "[0,0,1,2,2,3,3,3]\n" +
            "[0,0,1,2,2,3,3,3]\n" +
            "[0,1,1,2,2,3,3,3]\n" +
            "[0,1,1,2,2,3,4,4]\n";

      //
      assertEquals(s, i.getMatrix());
   }
}
