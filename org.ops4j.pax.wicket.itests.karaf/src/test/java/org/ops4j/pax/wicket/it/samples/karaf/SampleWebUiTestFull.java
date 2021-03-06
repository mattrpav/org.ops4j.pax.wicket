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
 */
package org.ops4j.pax.wicket.it.samples.karaf;

import java.io.BufferedReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.inject.Inject;
import org.apache.wicket.protocol.http.WebApplication;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.provision;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.wicket.samples.plain.simple.service.EchoService;
import org.osgi.framework.BundleContext;
import static shaded.org.apache.http.HttpHeaders.USER_AGENT;
import org.ops4j.pax.exam.util.Filter;
import org.ops4j.pax.wicket.api.WebApplicationFactory;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class SampleWebUiTestFull {

    /**
     * WebApplicationFactory of the some of the applications we started. We
     * don't use these members, except for synchronizing the test. Injecting
     * them guarantees that the services are available before our test runs. The
     * timeouts are rather high for the benefit of our CI server.
     */
    protected static final String WEBUI_PORT = "8181";
    protected static final String LOG_LEVEL = "WARN";
    protected static final String SYMBOLIC_NAME_PAX_WICKET_SERVICE = "org.ops4j.pax.wicket.service";

    private static final int TIMEOUT = 120 * 1000;

    @Inject
    private BundleContext bundleContext;

    @Inject
    @Filter(value = "(pax.wicket.applicationname=edge.inheritinjection)", timeout = TIMEOUT)
    private WebApplicationFactory<WebApplication> factoryEdgeInheritInjection;

    @Inject
    @Filter(value = "(pax.wicket.applicationname=springdm.simple.default)", timeout = TIMEOUT)
    private WebApplicationFactory<WebApplication> factorySpringDmSimpleDefault;
    @Inject
    @Filter(value = "(pax.wicket.applicationname=sample.ds.factory)", timeout = TIMEOUT)
    private WebApplicationFactory<WebApplication> factorySampleDS;
    @Configuration
    public final Option[] configureAdditionalProvision() {

        MavenUrlReference wicketFeatureRepo = maven()
                .groupId("org.ops4j.pax.wicket").artifactId("paxwicket")
                .classifier("features").type("xml").versionAsInProject();

        MavenUrlReference paxwicketFeatureRepo = maven()
                .groupId("org.ops4j.pax.wicket").artifactId("features")
                .classifier("features").type("xml").versionAsInProject();
        MavenUrlReference karafSampleFeatureRepo = maven()
                .groupId("org.ops4j.pax.wicket.samples").artifactId("features")
                .classifier("features").type("xml").versionAsInProject();
        MavenUrlReference karafStandardRepo = maven()
                .groupId("org.apache.karaf.features").artifactId("standard").classifier("features").type("xml").versionAsInProject();

        MavenArtifactUrlReference karafUrl = maven()
                .groupId("org.apache.karaf").artifactId("apache-karaf")
                .type("zip").versionAsInProject();

        return new Option[]{
            karafDistributionConfiguration()
            .frameworkUrl(karafUrl)
            .unpackDirectory(new File("target", "exam"))
            .useDeployFolder(false),
            keepRuntimeFolder(),
            configureConsole().ignoreLocalConsole(), logLevel(LogLevel.INFO),
            features(karafStandardRepo, "scr"),
            features(karafStandardRepo, "webconsole"),
            provision(mavenBundle().groupId("org.slf4j").artifactId("slf4j-simple").versionAsInProject().start(false)),
            features(wicketFeatureRepo, "wicket"),
            features(paxwicketFeatureRepo, "pax-wicket"),
            features(paxwicketFeatureRepo, "pax-wicket-blueprint"),
            features(karafSampleFeatureRepo, "wicket-samples-base"),
            features(karafSampleFeatureRepo, "wicket-samples-issues"),
            features(karafSampleFeatureRepo, "wicket-samples-plain-simple"),
            features(karafSampleFeatureRepo, "wicket-samples-plain-pagefactory"),
            features(karafSampleFeatureRepo, "wicket-samples-blueprint-simple"),
            features(karafSampleFeatureRepo, "wicket-samples-blueprint-wicketproperties"),
            features(karafSampleFeatureRepo, "wicket-samples-blueprint-mount"),
            features(karafSampleFeatureRepo, "wicket-samples-blueprint-filter"),
            features(karafSampleFeatureRepo, "wicket-samples-blueprint-applicationfactory"),
            features(karafSampleFeatureRepo, "wicket-samples-blueprint-injection-simple")
                ,
//SPRING stuff that pulls in cglib 3.x
            features(paxwicketFeatureRepo, "pax-wicket-spring"),
            features(karafSampleFeatureRepo, "wicket-samples-spring-injection-simple"),
            features(karafSampleFeatureRepo, "wicket-samples-spring-simple"),
            features(karafSampleFeatureRepo, "wicket-samples-edge-mixed"),
            features(karafSampleFeatureRepo, "wicket-samples-ds"),
            features(karafSampleFeatureRepo, "wicket-samples-edge-inheritinjection"),};

    }

    /**
     * used for manually testing put in @Test() and it will bring up a karaf with all samples loaded
     * @throws IOException 
     */
    //@Test()
    public void waitForever() throws IOException {
        //bundleContext.registerService(EchoService.class, new EchoServiceImplementation(), null);
        System.in.read();
    }

    @Test()
    public void allFactoriesPresent() throws Exception {

        assertNotNull(factorySpringDmSimpleDefault);
        assertNotNull(factoryEdgeInheritInjection);
        assertNotNull(factorySampleDS);
    }

    @Test
    public void testNavigationApplicationShouldRender() throws Exception {

        String page = sendGet("http://localhost:" + WEBUI_PORT + "/navigation/");
        assertTrue(page.contains("Homepage linking all OPS4J samples"));
    }

    @Test
    public void testSamplePlainSimpleShouldRenderPage() throws Exception {
        String page = sendGet("http://localhost:" + WEBUI_PORT + "/plain/simple/");
        assertTrue(page.contains("Welcome to the most simple pax-wicket application"));
    }

    @Test
    public void testSamplePlainPageFactoryShouldAllowLink() throws Exception {
        String page = sendGet("http://localhost:" + WEBUI_PORT + "/plain/pagefactory/");
        assertTrue(page.contains("Welcome to the most simple pax-wicket application"));

    }

    @Test
    public void checkInjectedPage() throws Exception {
        //Register a service here for later injection

        bundleContext.registerService(EchoService.class, new EchoServiceImplementation(), null);

        String page = sendGet("http://localhost:" + WEBUI_PORT + "/plain/inject/");
        assertTrue("/plain/inject/ failed to start properly", page.contains("Echo: Welcome to the most simple pax-wicket application"));

    }

    @Test
    public void testSampleBlueprintSimpleInjectionBluePrintShouldRenderPage() throws Exception {
        String page = sendGet("http://localhost:" + WEBUI_PORT + "/blueprint/injection/simple/");
        assertTrue(page.contains("Welcome to the most simple pax-wicket application based on blueprint"));
    }

    public void testSampleBlueprintSimpleDefaultShouldRenderPage() throws Exception {
        String page = sendGet("http://localhost:" + WEBUI_PORT + "/blueprint/simple/default");
        assertTrue(page.contains("Welcome to the most simple pax-wicket application based on blueprint"));
    }

    @Test
    public void testSampleBlueprintSimplePaxwicketShouldRenderPage() throws Exception {

        String page = sendGet("http://localhost:" + WEBUI_PORT + "/blueprint/simple/paxwicket");
        assertTrue(page.contains("Welcome to the most simple pax-wicket application based on blueprint"));

    }

    @Test
    public void testSampleBlueprintMountPointShouldRenderPage() throws Exception {
        String page = sendGet("http://localhost:" + WEBUI_PORT + "/blueprint/mount/manuallymounted");
        assertTrue(page.contains("This page is mounted manually."));
        page = sendGet("http://localhost:" + WEBUI_PORT + "/blueprint/mount/automounted");
        assertTrue(page.contains("This page is automatically mounted."));
        page = sendGet("http://localhost:" + WEBUI_PORT + "/blueprint/mount/initiallymounted");
        assertTrue(page.contains("This page is mounted initially."));
        page = sendGet("http://localhost:" + WEBUI_PORT + "/blueprint/mount");
        assertTrue(page.contains("Mountpoint blueprint based sample."));
    }

    @Test
    public void testSampleBlueprintMountPointsToSameApplication() throws Exception {
        String page = sendGet("http://localhost:" + WEBUI_PORT + "/blueprint/applicationfactory/first");
        assertTrue("contained :" + page, page.contains("This is the &#039;The first&#039; application home page."));
        page = sendGet("http://localhost:" + WEBUI_PORT + "/blueprint/applicationfactory/second");
        assertTrue("contained :" + page, page.contains("This is the &#039;The second&#039; application home page."));

    }

    @Test
    public void testSampleSpringdmInjectionSimpleShouldRenderPage() throws Exception {

        String page = sendGet("http://localhost:" + WEBUI_PORT + "/springdm/injection/simple");
        assertTrue(page.contains(
                "Welcome to the most simple pax-wicket injection application based on springdm."));

    }

    @Test
    public void testSampleSpringdmSimplePaxwicketShouldRenderPage() throws Exception {
        String page = sendGet("http://localhost:" + WEBUI_PORT + "/springdm/simple/paxwicket");
        assertTrue(page.contains("Welcome to the most simple pax-wicket application based on springdm"));

    }

    @Test
    public void testSampleMixedShouldRenderPage() throws Exception {

        String page = sendGet("http://localhost:" + WEBUI_PORT + "/mixed");
        assertTrue(page.contains(
                "Welcome to the mixed component and technology example. Enjoy the full power of pax wicket!."));
        assertTrue(page.contains("This is a link"));
        assertTrue(page.contains("This is a panel from a separate component"));

    }

    @Test
    public void testSampleEdgeInheritInjectionShouldRenderPage() throws Exception {
        String page = sendGet("http://localhost:" + WEBUI_PORT + "/edge/inheritinjection");
        assertTrue(page.contains("Back to parent"));
        assertTrue(page.contains("This is a link"));

    }

    @Test
    public void testSampleEdgeInheritInheritedPageShouldRenderPage() throws Exception {
        String page = sendGet("http://localhost:" + WEBUI_PORT + "/edge/inheritinjection/wicket/bookmarkable/org.ops4j.pax.wicket.samples.edge.inheritinjection.inherit.internal.InheritedPage");
        assertTrue(page.contains("Back to parent"));
        assertTrue(page.contains("This is a link"));
    }

    @Test
    public void testSampleDeclarativeServices() throws Exception {
        String page = sendGet("http://localhost:" + WEBUI_PORT + "/example/ds");
        assertTrue(page.contains("Declarative Services"));

    }

    private String sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();

    }

    /**
     * Simple Echo Implementation for itest...
     */
    private final class EchoServiceImplementation implements EchoService {

        private static final long serialVersionUID = 6447679249771482700L;

        public String someEchoMethod(String toEcho) {
            return "Echo: " + toEcho;
        }
    }
}
