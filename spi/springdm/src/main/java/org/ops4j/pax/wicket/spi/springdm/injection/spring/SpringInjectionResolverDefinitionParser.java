
/**
 * Copyright OPS4J
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author nmw
 * @version $Id: $Id
 */
package org.ops4j.pax.wicket.spi.springdm.injection.spring;

import org.ops4j.pax.wicket.api.PaxWicketBeanInjectionSource;
import org.ops4j.pax.wicket.spi.support.BundleInjectionProviderHelperDecorator;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;
public class SpringInjectionResolverDefinitionParser extends AbstractSpringBeanDefinitionParser {

    /** {@inheritDoc} */
    @Override
    public Class<?> getBeanClass(Element element) {
        return BundleInjectionProviderHelperDecorator.class;
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareInjection(Element element, BeanDefinitionBuilder builder) {
        addPropertyValueFromElement("applicationName", element, builder);
        builder.addPropertyValue("injectionSource", PaxWicketBeanInjectionSource.INJECTION_SOURCE_SPRING);
    }

}
