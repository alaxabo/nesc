// $Id$
/*									tab:4
 * Copyright (c) 2004-2005 Intel Corporation
 * All rights reserved.
 *
 * This file is distributed under the terms in the attached INTEL-LICENSE     
 * file. If you do not find these files, copies can be found by writing to
 * Intel Research Berkeley, 2150 Shattuck Avenue, Suite 1300, Berkeley, CA, 
 * 94704.  Attention:  Intel License Inquiry.
 */

package net.tinyos.nesc.dump.xml;

import net.tinyos.nesc.dump.*;
import org.xml.sax.*;
import java.util.*;

/**
 * A field in a structure. Note: these are always defined.
 */
public class Xfield extends Definition
{
    static protected DefinitionTable defs = new DefinitionTable();

    /**
     * Name of the field. Note that it may not be unique (because of
     * anonymous structs/unions collapsed into the containing struct).
     */
    public String name;

    /**
     * Unique identifier for this field.
     */
    public String ref;

    /**
     * Type of this field.
     */
    public Type type;

    /**
     * Structure this field belongs to.
     */
    public StructureDefinition container;

    /**
     * Offset in bits for this field from the start of the structure.
     * Note that this may be an UnknownConstant or a NonConstant.
     */
    public Constant bitOffset;

    /**
     * Size in bytes for this field.  Note that this may be an
     * UnknownConstant or a NonConstant.
     */
    public Constant size;

    /**
     * For bitfields only: size in bits for this field.  Note that
     * this may be an UnknownConstant (within generic components).
     */
    public Constant bitSize;

    /**
     * true if the gcc "packed" attribute was specified for this field.
     */
    public boolean packed;

    public void init(Attributes attrs) {
	ref = attrs.getValue("ref");
	name = attrs.getValue("field");
    }

    public synchronized NDElement start(Attributes attrs) {
	Xfield me = (Xfield)defs.define(attrs.getValue("ref"), attrs, this);
	me.bitOffset = Constant.decode(attrs.getValue("bit-offset"));
	me.packed = boolDecode(attrs.getValue("packed"));
	String s = attrs.getValue("size");
	if (s != null)
	    me.size = Constant.decode(s);
	s = attrs.getValue("bit-size");
	if (s != null)
	    me.bitSize = Constant.decode(s);

	return me;
    }

    static synchronized Definition lookup(NDReader reader, Attributes attrs) {
	return defs.lookup(reader, attrs.getValue("ref"), attrs, "field");
    }

    public void child(NDElement subElement) {
	super.child(subElement);
	if (subElement instanceof Type)
	    type = (Type)subElement;
    }
}