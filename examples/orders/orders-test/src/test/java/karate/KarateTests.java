package karate;

import com.intuit.karate.junit5.Karate;

class KarateTests {

  @Karate.Test
  Karate testOrdersAPI() {
    return Karate.run().relativeTo(getClass());
  }
}
