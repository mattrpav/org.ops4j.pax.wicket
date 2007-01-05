/*
 * Copyright 2006 Niclas Hedhman.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.wicket.internal;

import org.ops4j.lang.NullArgumentException;
import org.ops4j.pax.wicket.api.ContentSource;
import org.ops4j.pax.wicket.api.PageContentSource;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;

public class TrackingUtil
{

    public static Filter createContentFilter( BundleContext bundleContext, String applicationName )
        throws IllegalArgumentException
    {
        NullArgumentException.validateNotNull( bundleContext, "bundleContext" );
        NullArgumentException.validateNotEmpty( applicationName, "applicationName" );

        Filter filter;
        try
        {
            String filterString = "(&(" + ContentSource.APPLICATION_NAME + "=" + applicationName + ")"
                                  + "(" + Constants.OBJECTCLASS + "=" + ContentSource.class.getName() + "))";
            filter = bundleContext.createFilter( filterString );
        } catch( InvalidSyntaxException e )
        {
            throw new IllegalArgumentException( "applicationName can not contain '*', '(' or ')' : " + applicationName
            );
        }
        return filter;
    }

    public static Filter createAllPageContentFilter( BundleContext bundleContext, String applicationName )
        throws IllegalArgumentException
    {
        NullArgumentException.validateNotNull( bundleContext, "bundleContext" );
        NullArgumentException.validateNotEmpty( applicationName, "applicationName" );

        Filter filter;
        try
        {
            String filterString = "(&(" + ContentSource.APPLICATION_NAME + "=" + applicationName + ")"
                                  + "(" + Constants.OBJECTCLASS + "=" + PageContentSource.class.getName() + "))";
            filter = bundleContext.createFilter( filterString );
        } catch( InvalidSyntaxException e )
        {
            throw new IllegalArgumentException( "applicationName can not contain '*', '(' or ')' : " + applicationName
            );
        }
        return filter;
    }

}