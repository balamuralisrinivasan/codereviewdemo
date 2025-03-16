package com.example.inventorymanagement.performance;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * JMeter test plan generator for performance testing.
 * This class generates JMeter test plans for API performance testing.
 */
public class JMeterTestPlanGenerator {

    /**
     * Generates a JMeter test plan for the Product API.
     */
    public static void generateProductApiTestPlan(String outputPath, String baseUrl) throws Exception {
        // Initialize JMeter
        File jmeterHome = Files.createTempDirectory("jmeter").toFile();
        String jmeterProperties = JMeterTestPlanGenerator.class.getClassLoader()
                .getResource("jmeter.properties").getFile();
        JMeterUtils.loadJMeterProperties(jmeterProperties);
        JMeterUtils.setJMeterHome(jmeterHome.getPath());
        JMeterUtils.initLocale();

        // Create a JMeter Test Plan
        TestPlan testPlan = new TestPlan("Product API Test Plan");
        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

        // Create HTTP Header Manager
        HeaderManager headerManager = new HeaderManager();
        headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
        headerManager.setName("HTTP Headers");
        headerManager.add(new Header("Content-Type", "application/json"));
        headerManager.add(new Header("Accept", "application/json"));

        // Create Thread Group
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());
        threadGroup.setName("Product API Thread Group");
        threadGroup.setNumThreads(10);
        threadGroup.setRampUp(5);
        threadGroup.setSamplerController(createLoopController(5));

        // Create HTTP Sampler for GET all products
        HTTPSamplerProxy getAllProductsSampler = createHttpSampler(
                "GET All Products",
                baseUrl,
                "/api/products",
                "GET"
        );

        // Create HTTP Sampler for GET product by ID
        HTTPSamplerProxy getProductByIdSampler = createHttpSampler(
                "GET Product by ID",
                baseUrl,
                "/api/products/1",
                "GET"
        );

        // Create HTTP Sampler for search products
        HTTPSamplerProxy searchProductsSampler = createHttpSampler(
                "Search Products",
                baseUrl,
                "/api/products/search?name=Test",
                "GET"
        );

        // Construct Test Plan tree
        HashTree testPlanTree = new HashTree();
        HashTree threadGroupTree = testPlanTree.add(testPlan).add(threadGroup);
        threadGroupTree.add(headerManager);
        threadGroupTree.add(getAllProductsSampler);
        threadGroupTree.add(getProductByIdSampler);
        threadGroupTree.add(searchProductsSampler);

        // Save the test plan to a JMX file
        Path outputFilePath = Paths.get(outputPath);
        Files.createDirectories(outputFilePath.getParent());
        SaveService.saveTree(testPlanTree, new FileOutputStream(outputPath));

        System.out.println("JMeter Test Plan generated: " + outputPath);
    }

    /**
     * Creates a loop controller for the thread group.
     */
    private static LoopController createLoopController(int loops) {
        LoopController loopController = new LoopController();
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
        loopController.setLoops(loops);
        loopController.setFirst(true);
        return loopController;
    }

    /**
     * Creates an HTTP sampler for the test plan.
     */
    private static HTTPSamplerProxy createHttpSampler(String name, String domain, String path, String method) {
        HTTPSamplerProxy httpSampler = new HTTPSamplerProxy();
        httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
        httpSampler.setName(name);
        httpSampler.setDomain(domain);
        httpSampler.setPath(path);
        httpSampler.setMethod(method);
        httpSampler.setFollowRedirects(true);
        httpSampler.setUseKeepAlive(true);
        return httpSampler;
    }

    /**
     * Main method to generate test plans.
     */
    public static void main(String[] args) {
        try {
            generateProductApiTestPlan("target/jmeter/product-api-test-plan.jmx", "localhost");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 