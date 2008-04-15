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
package org.ops4j.pax.wicket.samples.departmentstore.service.alternative.internal;

import org.ops4j.pax.wicket.samples.departmentstore.model.DepartmentStore;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator
    implements BundleActivator
{
    private AlternativeDepartmentStoreImpl m_departmentStore;
    private ServiceRegistration m_registration;

    public void start( BundleContext bundleContext )
        throws Exception
    {
        m_departmentStore = new AlternativeDepartmentStoreImpl( "Main Market" );
        m_registration = bundleContext.registerService( DepartmentStore.class.getName(), m_departmentStore, null );
    }

    public void stop( BundleContext bundleContext )
        throws Exception
    {
        m_registration.unregister();
    }
}