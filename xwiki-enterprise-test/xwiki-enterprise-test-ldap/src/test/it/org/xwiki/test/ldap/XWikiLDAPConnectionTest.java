/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
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
package org.xwiki.test.ldap;

import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiContext;
import org.xwiki.test.ldap.framework.AbstractLDAPTestCase;
import org.xwiki.test.ldap.framework.LDAPTestSetup;
import com.xpn.xwiki.plugin.ldap.XWikiLDAPConnection;
import com.xpn.xwiki.plugin.ldap.XWikiLDAPException;

/**
 * Tests {@link XWikiLDAPConnection}.
 * 
 * @version $Id$
 */
public class XWikiLDAPConnectionTest extends AbstractLDAPTestCase
{
    /**
     * The XWiki context.
     */
    private XWikiContext context;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        this.context = new XWikiContext();

        this.context.setWiki(new XWiki());
    }

    /**
     * Test open and close of the LDAP connection.
     * 
     * @throws XWikiLDAPException
     */
    public void testOpenClose() throws XWikiLDAPException
    {
        int port = LDAPTestSetup.getLDAPPort();

        XWikiLDAPConnection connection = new XWikiLDAPConnection();

        assertTrue("LDAP connection failed", connection.open("localhost", port, LDAPTestSetup.HORATIOHORNBLOWER_DN,
            LDAPTestSetup.HORATIOHORNBLOWER_PWD, null, false, context));

        connection.close();
    }
}
