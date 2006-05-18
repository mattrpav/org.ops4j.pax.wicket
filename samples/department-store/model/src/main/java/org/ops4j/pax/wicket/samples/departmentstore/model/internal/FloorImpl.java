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
package org.ops4j.pax.wicket.samples.departmentstore.model.internal;

import java.util.List;
import java.util.ArrayList;
import org.ops4j.pax.wicket.samples.departmentstore.model.Floor;
import org.ops4j.pax.wicket.samples.departmentstore.model.Franchisee;

public class FloorImpl implements Floor
{

    private String m_name;
    private List<Franchisee> m_franchisees;

    public FloorImpl( String name )
    {
        m_name = name;
        m_franchisees = new ArrayList<Franchisee>();
    }

    public String getName()
    {
        return m_name;
    }

    public List<Franchisee> getFranchisees()
    {
        return m_franchisees;
    }

    public void addFranchisee( Franchisee franchisee )
    {
        m_franchisees.add( franchisee );
    }

    public void removeFranchise( Franchisee franchisee )
    {
        m_franchisees.remove( franchisee );
    }
}
