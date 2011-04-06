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

package org.exoplatform.treesync;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class DiffTestCase extends TestCase {

   public void testSyncException() {
      SimpleNode node1 = new SimpleNode();
      SimpleNode node2 = new SimpleNode();
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(SimpleNodeModel.INSTANCE, node1, SimpleNodeModel.INSTANCE, node2);
      BufferHandler handler = new BufferHandler();
      try {
         diff.perform(handler);
         fail();
      } catch (SyncException e) {
      }
   }

   public void testEmpty() throws Exception {
      SimpleNode node1 = new SimpleNode();
      SimpleNode node2 = node1.clone();
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(SimpleNodeModel.INSTANCE, node1, SimpleNodeModel.INSTANCE, node2);
      BufferHandler handler = new BufferHandler();
      diff.perform(handler);
      handler.assertEnter(node1, node2);
      handler.assertLeave(node1, node2);
      handler.assertEmpty();
   }

   public void testFoo() throws Exception {
      SimpleNode node1 = new SimpleNode();
      SimpleNode child1 = node1.addChild("foo");
      SimpleNode node2 = node1.clone();
      SimpleNode child2 = node2.getChild(child1.getId());
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(SimpleNodeModel.INSTANCE, node1, SimpleNodeModel.INSTANCE, node2);
      BufferHandler handler = new BufferHandler();
      diff.perform(handler);
      handler.assertEnter(node1, node2);
      handler.assertEnter(child1, child2);
      handler.assertLeave(child1, child2);
      handler.assertLeave(node1, node2);
      handler.assertEmpty();
   }

   public void testRemove() throws Exception {
      SimpleNode node1 = new SimpleNode();
      SimpleNode child1 = node1.addChild("foo");
      SimpleNode node2 = node1.clone();
      node2.getChild(child1.getId()).destroy();
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(SimpleNodeModel.INSTANCE, node1, SimpleNodeModel.INSTANCE, node2);
      BufferHandler handler = new BufferHandler();
      diff.perform(handler);
      handler.assertEnter(node1, node2);
      handler.assertRemoved(child1);
      handler.assertLeave(node1, node2);
      handler.assertEmpty();
   }

   public void testAdd() throws Exception {
      SimpleNode node1 = new SimpleNode();
      SimpleNode node2 = node1.clone();
      SimpleNode child2 = node2.addChild("foo");
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(SimpleNodeModel.INSTANCE, node1, SimpleNodeModel.INSTANCE, node2);
      BufferHandler handler = new BufferHandler();
      diff.perform(handler);
      handler.assertEnter(node1, node2);
      handler.assertAdded(child2);
      handler.assertLeave(node1, node2);
      handler.assertEmpty();
   }

   public void testMove() throws Exception {
      SimpleNode root1 = new SimpleNode();
      SimpleNode a1 = root1.addChild("a");
      SimpleNode b1 = root1.addChild("b");
      SimpleNode c1 = a1.addChild("c");
      SimpleNode root2 = root1.clone();
      SimpleNode a2 = root2.getChild(a1.getId());
      SimpleNode b2 = root2.getChild(b1.getId());
      SimpleNode c2 = a2.getChild(c1.getId());
      b2.addChild(c2);
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(SimpleNodeModel.INSTANCE, root1, SimpleNodeModel.INSTANCE, root2);
      BufferHandler handler = new BufferHandler();
      diff.perform(handler);
      handler.assertEnter(root1, root2);
      handler.assertEnter(a1, a2);
      handler.assertMovedOut(c1, c2);
      handler.assertLeave(a1, a2);
      handler.assertEnter(b1, b2);
      handler.assertMovedIn(c1, c2);
      handler.assertEnter(c1, c2);
      handler.assertLeave(c1, c2);
      handler.assertLeave(b1, b2);
      handler.assertLeave(root1, root2);
      handler.assertEmpty();
   }

   public void testRecurseOnMove() throws Exception {
      SimpleNode root1 = new SimpleNode();
      SimpleNode a1 = root1.addChild("a");
      SimpleNode b1 = root1.addChild("b");
      SimpleNode c1 = a1.addChild("c");
      SimpleNode d1 = c1.addChild("d");
      SimpleNode root2 = root1.clone();
      SimpleNode a2 = root2.getChild(a1.getId());
      SimpleNode b2 = root2.getChild(b1.getId());
      SimpleNode c2 = a2.getChild(c1.getId());
      SimpleNode d2 = c2.getChild(d1.getId());
      b2.addChild(c2);
      root2.addChild(d2);
      Diff<SimpleNode, SimpleNode> diff = new Diff<SimpleNode, SimpleNode>(SimpleNodeModel.INSTANCE, root1, SimpleNodeModel.INSTANCE, root2);
      BufferHandler handler = new BufferHandler();
      diff.perform(handler);
      handler.assertEnter(root1, root2);
      handler.assertEnter(a1, a2);
      handler.assertMovedOut(c1, c2);
      handler.assertLeave(a1, a2);
      handler.assertEnter(b1, b2);
      handler.assertMovedIn(c1, c2);
      handler.assertEnter(c1, c2);
      handler.assertMovedOut(d1, d2);
      handler.assertLeave(c1, c2);
      handler.assertLeave(b1, b2);
      handler.assertMovedIn(d1, d2);
      handler.assertEnter(d1, d2);
      handler.assertLeave(d1, d2);
      handler.assertLeave(root1, root2);
      handler.assertEmpty();
   }
}
