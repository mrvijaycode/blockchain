package com.unigem.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testServer() throws Exception {
		EnrollAdmin.main(null);
		RegisterUser.main(null);
	}

}
