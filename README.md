
# OLX Scraper

A tool for those who (like me) spend a lot of time looking for useless things on OLX (a kind of Brazilian craigslist)

After dedicating a few good hours to studies related to Web Scraping, Groovy with its various libraries and building APIs with Spring, I decided to improve one of the projects proposed in the process of selecting internships at ZG Soluções.

When it was presented to me, the project, which was relatively simple, was a Web Scraper of ads published on the OLX website (https://www.olx.com.br), running locally and saving the ads found in a spreadsheet at the end of its execution. My proposal for improvement concerns the construction of an API using Spring, where the user informs the title of the advertisement and the state of Brazil in which it is located and then executes the request, in order to obtain the response that consists of several information such as average price of the searched ad, cheapest ad, most expensive ad, number of ads found and finally, a vast list of ads containing their title, value, address and URL.

Reviews and suggestions are always welcome.

## Main URL:
https://olx-scraper.herokuapp.com

## Stack utilized

**Backend:** Spring with Groovy and JSOUP.


## API documentation

#### Main requisition

```http
  POST /item
```

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `RequestModel` | `json`  |  The parameter will be on the body of the requisition.|

#### Example
```json
{
    "state":"go",
    "title":"Fiat Argo"
}
```

#### Returns a response with a lot (A LOT) of informations.

```json
{
    "searchTitle": "Fiat Argo",
    "avaragePrice": 367322.08,
    "cheapestItem": {
        "title": "FIAT ARGO 2018 ( PARCELO NO BOLETO )",
        "value": 56.0,
        "address": "Goiânia, Setor Central - DDD 62",
        "adURL": "https://go.olx.com.br/grande-goiania-e-anapolis/autos-e-pecas/pecas-e-acessorios/carros-vans-e-utilitarios/fiat-argo-2018-parcelo-no-boleto-1118460572"
    },
    "moreExpensiveItem": {
        "title": "Fiat Argo 1.0",
        "value": 4.5E7,
        "address": "Itapaci - DDD 62",
        "adURL": "https://go.olx.com.br/grande-goiania-e-anapolis/autos-e-pecas/carros-vans-e-utilitarios/fiat-argo-1-0-1118389270"
    },
    "adAmount": 145,
    "advertisements": [
        {
            "title": "FIAT ARGO 1.0 FIREFLY FLEX DRIVE MANUAL",
            "value": 65644.0,
            "address": "Goiânia, Setor Aeroporto - DDD 62",
            "adURL": "https://go.olx.com.br/grande-goiania-e-anapolis/autos-e-pecas/carros-vans-e-utilitarios/fiat-argo-1-0-firefly-flex-drive-manual-1121983631"
        },
        ...,
        {
            "title": "FIAT ARGO 2021/2021 1.0 FIREFLY FLEX MANUAL",
            "value": 60380.0,
            "address": "Goiânia, Setor Marista - DDD 62",
            "adURL": "https://go.olx.com.br/grande-goiania-e-anapolis/autos-e-pecas/carros-vans-e-utilitarios/fiat-argo-2021-2021-1-0-firefly-flex-manual-1123260717"
        }
    ]
}
```


## Running locally

Clone the project

```bash
  git clone https://github.com/mourarezendecas/olx-scraper
```

Navigate to the project file

```bash
  cd olx-scraper
```

Run the application

```bash
  mvn spring-boot:run
```

