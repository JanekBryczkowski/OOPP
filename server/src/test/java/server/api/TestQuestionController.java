package server.api;

public class TestQuestionController {
    /*public int nextInt;
    private MyRandom random;
    private TestQuoteRepository repo;

    private QuoteController sut;

    @BeforeEach
    public void setup() {
        random = new MyRandom();
        repo = new TestQuoteRepository();
        sut = new QuoteController(random, repo);
    }

    @Test
    public void cannotAddNullPerson() {
        var actual = sut.add(getQuote(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void randomSelection() {
        sut.add(getQuote("q1"));
        sut.add(getQuote("q2"));
        nextInt = 1;
        var actual = sut.getRandom();

        assertTrue(random.wasCalled);
        assertEquals("q2", actual.getBody().quote);
    }

    @Test
    public void databaseIsUsed() {
        sut.add(getQuote("q1"));
        repo.calledMethods.contains("save");
    }

    private static Quote getQuote(String q) {
        return new Quote(new Person(q, q), q);
    }

    @SuppressWarnings("serial")
    public class MyRandom extends Random {

        public boolean wasCalled = false;

        @Override
        public int nextInt(int bound) {
            wasCalled = true;
            return nextInt;
        }
    }*/
}
