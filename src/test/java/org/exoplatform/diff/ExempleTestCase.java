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

package org.exoplatform.diff;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.exoplatform.diff.list.ListChangeIterator;
import org.exoplatform.diff.list.ListChangeType;
import org.exoplatform.diff.list.ListDiff;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 */
public class ExempleTestCase extends TestCase {

   public void testList() {
      String[] source = new String[]{"banana","strawberry","cherry"};
      List<String> destintation = Arrays.asList("apple", "banana", "raspberry","cherry");
      ListDiff<String[], List<String>, String> diff = new ListDiff<String[], List<String>, String>(new ArrayAdapter<String>(), new JavaUtilListAdapter<String>());
      ListChangeIterator<String[], List<String>, String> it = diff.iterator(source, destintation);
      Assert.assertEquals(ListChangeType.ADD, it.next());
      assertEquals("apple", it.getElement());
      assertEquals(ListChangeType.SAME, it.next());
      assertEquals("banana", it.getElement());
      assertEquals(ListChangeType.ADD, it.next());
      assertEquals("raspberry", it.getElement());
      assertEquals(ListChangeType.REMOVE, it.next());
      assertEquals("strawberry", it.getElement());
      assertEquals(ListChangeType.SAME, it.next());
      assertEquals("cherry", it.getElement());
      assertFalse(it.hasNext());
   }
}
