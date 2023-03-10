package com.mourarezendecas.olxscraper

import com.mourarezendecas.olxscraper.models.InvalidResponseModel
import com.mourarezendecas.olxscraper.models.RequestModel
import com.mourarezendecas.olxscraper.models.ResponseModel
import com.mourarezendecas.olxscraper.services.ResponseService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue;


@SpringBootTest
class OlxScraperApplicationTests {
	LinkedHashMap<String,String> invalidSearch = [title:'invalid_search',state:'go']
	LinkedHashMap<String,String> validSearch = [title:'iphone 11',state:'go']

	@Test
	void 'priceConverter deve corrigir o valor do anúncio para Double'() {
		String input = "189.900"
		double expectedOutput = 189900
		double actualOutput = ResponseService.priceToDouble(input)
		assertEquals(expectedOutput,actualOutput,0.001)
	}

	@Test
	void 'collect se comporta de maneira esperada quando busca de maneira inválida'(){
		GroovyObject actualOutput = ResponseService.scrape(new RequestModel(invalidSearch))
		InvalidResponseModel expectedOutput = new InvalidResponseModel()
		assertEquals(actualOutput.title,expectedOutput.title)
	}

	@Test
	void 'confere se a busca é válida através de seu título'(){
		GroovyObject actualOutput = ResponseService.scrape(new RequestModel(validSearch))
		def expectedOutput = new ResponseModel(searchTitle: validSearch.title)
		assertEquals(actualOutput.searchTitle,expectedOutput.searchTitle)
	}

	@Test
	void 'quando a busca é valida, ela retorna uma lista de anuncios maior que zero'(){
		GroovyObject actualOutput = ResponseService.scrape(new RequestModel(validSearch))
		assertTrue(actualOutput.adAmount > 0)
	}
}
