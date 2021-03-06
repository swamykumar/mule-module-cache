/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.cache;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.client.DefaultLocalMuleClient;
import org.mule.tck.FunctionalTestCase;

public class ExpressionTest extends FunctionalTestCase
{
    String cacheable1 = "<root><cacheable>true</cacheable><key>1</key></root>";
    String cacheable2 = "<root><cacheable>true</cacheable><key>2</key></root>";
    String notCacheable = "<root><cacheable>false</cacheable></root>";

    public void testExternalGenerator() throws Exception
    {
        testMessages("vm://test1");
    }

    public void xtestExpressionInline() throws Exception
    {
        testMessages("vm://test2");
    }

    public void testMessages(String endpoint) throws Exception
    {
        MuleClient client = new DefaultLocalMuleClient(muleContext);

        MuleMessage msg = client.send(endpoint, new DefaultMuleMessage(cacheable1, muleContext));
        assertEquals(0, msg.getPayload());

        msg = client.send(endpoint, new DefaultMuleMessage(cacheable2, muleContext));
        assertEquals(1, msg.getPayload());

        msg = client.send(endpoint, new DefaultMuleMessage(cacheable1, muleContext));
        assertEquals(0, msg.getPayload());

        msg = client.send(endpoint, new DefaultMuleMessage(cacheable2, muleContext));
        assertEquals(1, msg.getPayload());

        msg = client.send(endpoint, new DefaultMuleMessage(notCacheable, muleContext));
        assertEquals(2, msg.getPayload());

        msg = client.send(endpoint, new DefaultMuleMessage(notCacheable, muleContext));
        assertEquals(3, msg.getPayload());

    }

    @Override
    protected String getConfigResources()
    {
        return "expression-config.xml";
    }

}
