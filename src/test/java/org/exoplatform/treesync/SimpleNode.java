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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class SimpleNode {

  /** . */
  private static final AtomicInteger generator = new AtomicInteger();

  /** . */
  private final String id;

  /** . */
  private String state;

  /** . */
  private final List<SimpleNode> children;

  public SimpleNode() {
    this(null);
  }

  public SimpleNode(String state)
  {
    this.id = "" + generator.incrementAndGet();
    this.children = new ArrayList<SimpleNode>();
    this.state = state;
  }

  public String getId() {
    return id;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public SimpleNode addChild(String state) {
    return new SimpleNode(state);
  }

  public List<SimpleNode> getChildren() {
    return new ArrayList<SimpleNode>(children);
  }

  public SimpleNode clone() {
    try {
      return (SimpleNode)super.clone();
    }
    catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof SimpleNode) {
      SimpleNode that = (SimpleNode)o;
      if ((state != null && state.equals(that.state)) || (state == null && that.state == null)) {
        return children.equals(that.children);
      }
    }
    return false;
  }
}
