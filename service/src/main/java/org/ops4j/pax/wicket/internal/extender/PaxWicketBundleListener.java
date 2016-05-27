
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
package org.ops4j.pax.wicket.internal.extender;

import org.ops4j.pax.wicket.internal.extender.ExtendedBundle.ExtendedBundleContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public abstract class PaxWicketBundleListener implements BundleTrackerCustomizer<ExtendedBundle> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaxWicketBundleListener.class);

    private ExtendedBundleContext extendedBundleContext;

    /** {@inheritDoc} */
    @Override
    public ExtendedBundle addingBundle(Bundle bundle, BundleEvent event) {
        ExtendedBundle extendedBundle =
            new ExtendedBundle(extendedBundleContext, bundle);
        if (extendedBundle.isImportingPAXWicketAPI() || extendedBundle.isImportingWicket()) {
            addRelevantBundle(extendedBundle);
            LOGGER.info("{} is added as a relevant bundle for pax wicket", bundle.getSymbolicName());
            return extendedBundle;
        } else {
            // No need to track this...
            return null;
        }
    }

    /**
     * <p>modifiedBundle.</p>
     *
     * @param bundle a {@link org.osgi.framework.Bundle} object.
     * @param event a {@link org.osgi.framework.BundleEvent} object.
     * @param object a {@link org.ops4j.pax.wicket.internal.extender.ExtendedBundle} object.
     */
    public void modifiedBundle(Bundle bundle, BundleEvent event, ExtendedBundle object) {
        // we don't care about state changes (for now)
    }

    /** {@inheritDoc} */
    @Override
    public void removedBundle(Bundle bundle, BundleEvent event, ExtendedBundle object) {
        removeRelevantBundle(object);
        LOGGER.debug("{} is removed as a relevant bundle for pax wicket", bundle.getSymbolicName());
    }

    /**
     * <p>addRelevantBundle.</p>
     *
     * @param bundle a {@link org.ops4j.pax.wicket.internal.extender.ExtendedBundle} object.
     */
    protected abstract void addRelevantBundle(ExtendedBundle bundle);

    /**
     * <p>removeRelevantBundle.</p>
     *
     * @param bundle a {@link org.ops4j.pax.wicket.internal.extender.ExtendedBundle} object.
     */
    protected abstract void removeRelevantBundle(ExtendedBundle bundle);

    /**
     * <p>activate.</p>
     *
     * @param bundleContext a {@link org.osgi.framework.BundleContext} object.
     * @since 3.0.5
     */
    protected void activate(BundleContext bundleContext) {
        extendedBundleContext = new ExtendedBundle.ExtendedBundleContext(bundleContext);
    }

}
