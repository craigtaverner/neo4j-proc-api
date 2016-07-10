package org.amanzi.neo4j.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.QueryExecutionException;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.test.TestGraphDatabaseFactory;

import static org.amanzi.neo4j.api.TestUtil.testCall;
import static org.amanzi.neo4j.api.TestUtil.testCallEmpty;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ProcAPITest {

    private GraphDatabaseService db;

    @Before
    public void setUp() throws Exception {
        db = new TestGraphDatabaseFactory().newImpermanentDatabase();
        ((GraphDatabaseAPI) db).getDependencyResolver().resolveDependency(Procedures.class).register(AmanziProcedures.class);
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void testCountNodes() {
        testCallReturnsValue("CALL amanzi.countNodes", 0L, "No nodes were expected");
    }

    @Test
    public void testCreateNodes() {
        testCallReturnsValue("CALL amanzi.makeNode('Test')", 0L, "Expected first node to have id=0");
        testCallReturnsValue("CALL amanzi.makeNode('Test')", 1L, "Expected second node to have id=1");
        testCallReturnsValue("CALL amanzi.makeNode('Test')", 2L, "Expected third node to have id=2");
        testCallReturnsValue("CALL amanzi.countNodes", 3L, "Three nodes were expected");
    }

    @Test
    public void testFailingProcedure() {
        try {
            testCallEmpty(db, "CALL amanzi.willFail", null);
        } catch (QueryExecutionException e) {
            assertThat(e.getMessage(), containsString("Write operations are not allowed"));
        } catch (Throwable t) {
            fail("Expected QueryExecutionException but got " + t.getClass().getSimpleName() + ": " + t.getMessage());
        }
    }

    private void testCallReturnsValue(String query, long expected, String failMsg) {
        testCall(db, query, null,
                (row) -> {
                    long value = (Long) row.get("value");
                    assertEquals(failMsg, expected, value);
                });
    }
}
