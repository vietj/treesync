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

package org.exoplatform.treesync;

import junit.framework.TestCase;

import static org.exoplatform.treesync.SimpleModel.read;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Diff2TestCase extends TestCase {

   public void testSyncException() {
      SimpleNode node1 = new SimpleNode();
      SimpleNode node2 = new SimpleNode();
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(read(node1), read(node2));
      DiffChangeIterator<SimpleNode, SimpleNode> it = diff.iterator();
      assertEquals(DiffChangeType.ERROR, it.next());
      assertSame(node1, it.getSource());
      assertSame(node2, it.getDestination());
      assertFalse(it.hasNext());
   }

   public void testEmpty() throws Exception {
      SimpleNode node1 = new SimpleNode();
      SimpleNode node2 = node1.clone();
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(read(node1), read(node2));
      DiffChangeIterator<SimpleNode, SimpleNode> it = diff.iterator();
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(node1, it.getSource());
      assertSame(node2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(node1, it.getSource());
      assertSame(node2, it.getDestination());
      assertFalse(it.hasNext());
   }

   public void testFoo() throws Exception {
      SimpleNode node1 = new SimpleNode();
      SimpleNode child1 = node1.addChild();
      SimpleNode node2 = node1.clone();
      SimpleNode child2 = node2.getChild(child1.getId());
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(read(node1), read(node2));
      DiffChangeIterator<SimpleNode, SimpleNode> it = diff.iterator();
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(node1, it.getSource());
      assertSame(node2, it.getDestination());
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(child1, it.getSource());
      assertSame(child2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(child1, it.getSource());
      assertSame(child2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(node1, it.getSource());
      assertSame(node2, it.getDestination());
      assertFalse(it.hasNext());
   }

   public void testRemove() throws Exception {
      SimpleNode node1 = new SimpleNode();
      SimpleNode child1 = node1.addChild();
      SimpleNode node2 = node1.clone();
      node2.getChild(child1.getId()).destroy();
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(read(node1), read(node2));
      DiffChangeIterator<SimpleNode, SimpleNode> it = diff.iterator();
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(node1, it.getSource());
      assertSame(node2, it.getDestination());
      assertEquals(DiffChangeType.REMOVED, it.next());
      assertSame(child1, it.getSource());
      assertSame(null, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(node1, it.getSource());
      assertSame(node2, it.getDestination());
      assertFalse(it.hasNext());
   }

   public void testAdd() throws Exception {
      SimpleNode node1 = new SimpleNode();
      SimpleNode node2 = node1.clone();
      SimpleNode child2 = node2.addChild();
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(read(node1), read(node2));
      DiffChangeIterator<SimpleNode, SimpleNode> it = diff.iterator();
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(node1, it.getSource());
      assertSame(node2, it.getDestination());
      assertEquals(DiffChangeType.ADDED, it.next());
      assertSame(null, it.getSource());
      assertSame(child2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(node1, it.getSource());
      assertSame(node2, it.getDestination());
      assertFalse(it.hasNext());
   }
   public void testMove() throws Exception {
      SimpleNode root1 = new SimpleNode();
      SimpleNode a1 = root1.addChild();
      SimpleNode b1 = root1.addChild();
      SimpleNode c1 = a1.addChild();
      SimpleNode root2 = root1.clone();
      SimpleNode a2 = root2.getChild(a1.getId());
      SimpleNode b2 = root2.getChild(b1.getId());
      SimpleNode c2 = a2.getChild(c1.getId());
      b2.addChild(c2);
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(read(root1), read(root2));
      DiffChangeIterator<SimpleNode, SimpleNode> it = diff.iterator();
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(root1, it.getSource());
      assertSame(root2, it.getDestination());
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(a1, it.getSource());
      assertSame(a2, it.getDestination());
      assertEquals(DiffChangeType.MOVED_OUT, it.next());
      assertSame(c1, it.getSource());
      assertSame(c2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(a1, it.getSource());
      assertSame(a2, it.getDestination());
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(b1, it.getSource());
      assertSame(b2, it.getDestination());
      assertEquals(DiffChangeType.MOVED_IN, it.next());
      assertSame(c1, it.getSource());
      assertSame(c2, it.getDestination());
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(c1, it.getSource());
      assertSame(c2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(c1, it.getSource());
      assertSame(c2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(b1, it.getSource());
      assertSame(b2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(root1, it.getSource());
      assertSame(root2, it.getDestination());
      assertFalse(it.hasNext());
   }

   public void testRecurseOnMove() throws Exception {
      SimpleNode root1 = new SimpleNode();
      SimpleNode a1 = root1.addChild();
      SimpleNode b1 = root1.addChild();
      SimpleNode c1 = a1.addChild();
      SimpleNode d1 = c1.addChild();
      SimpleNode root2 = root1.clone();
      SimpleNode a2 = root2.getChild(a1.getId());
      SimpleNode b2 = root2.getChild(b1.getId());
      SimpleNode c2 = a2.getChild(c1.getId());
      SimpleNode d2 = c2.getChild(d1.getId());
      b2.addChild(c2);
      root2.addChild(d2);
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(read(root1), read(root2));
      DiffChangeIterator<SimpleNode, SimpleNode> it = diff.iterator();
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(root1, it.getSource());
      assertSame(root2, it.getDestination());
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(a1, it.getSource());
      assertSame(a2, it.getDestination());
      assertEquals(DiffChangeType.MOVED_OUT, it.next());
      assertSame(c1, it.getSource());
      assertSame(c2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(a1, it.getSource());
      assertSame(a2, it.getDestination());
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(b1, it.getSource());
      assertSame(b2, it.getDestination());
      assertEquals(DiffChangeType.MOVED_IN, it.next());
      assertSame(c1, it.getSource());
      assertSame(c2, it.getDestination());
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(c1, it.getSource());
      assertSame(c2, it.getDestination());
      assertEquals(DiffChangeType.MOVED_OUT, it.next());
      assertSame(d1, it.getSource());
      assertSame(d2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(c1, it.getSource());
      assertSame(c2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(b1, it.getSource());
      assertSame(b2, it.getDestination());
      assertEquals(DiffChangeType.MOVED_IN, it.next());
      assertSame(d1, it.getSource());
      assertSame(d2, it.getDestination());
      assertEquals(DiffChangeType.ENTER, it.next());
      assertSame(d1, it.getSource());
      assertSame(d2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(d1, it.getSource());
      assertSame(d2, it.getDestination());
      assertEquals(DiffChangeType.LEAVE, it.next());
      assertSame(root1, it.getSource());
      assertSame(root2, it.getDestination());
      assertFalse(it.hasNext());
   }
}
