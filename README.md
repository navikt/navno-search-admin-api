# navno-search-admin-api
Backend for å administrere og populere Opensearch-index som brukes i søk på nav.no.

Secrets ligger i [Nais console](https://console.nav.cloud.nais.io/team/personbruker/secrets).

Les mer om indeksering i [navno-search-api](https://github.com/navikt/navno-search-api).

## Lokal kjøring
For å kjøre appen lokalt må man opprette en application-local.yml-fil og populere denne med følgende  (opensearch-credentials ligger i kubernetes).

```
opensearch:
  uris: <uri fra secret>
  username: <brukernavn fra secret>
  password: <passord fra secret>

kodeverk:
  spraak:
    url: https://kodeverk.dev-fss-pub.nais.io/api/v1/kodeverk/Språk/koder
  scope: kodeverk-scope

no.nav.security.jwt:
  issuer:
    azuread:
      accepted-audience: someaudience
      client-secret: somesecret
      token-endpoint: someendpoint
      
api-key: dummy
```

Husk å starte applikasjonen med profile "local".

## Logs
I Opensearch (uri ligger i ```application-local.yaml``` filen) kan man finne logger ved å f.eks. søke på

```
GET /_cat/indices

DELETE /search-content-v7

GET /search-content-v6

GET /search-content-v6/_mapping

POST /search-content-v6/_analyze
{
  "field": "title.no",
  "text": "skule"
}

POST /search-content-v6/_search
{
  "query": {
    "match": {
      "title.no": "nav midt-buskerud"
    }
  }
}
```

## Deploy til dev

[Actions](https://github.com/navikt/navno-search-admin-api/actions) -> Velg workflow -> Run workflow -> Velg branch -> Run workflow

## Prodsetting

-   Lag en PR til main, og merge inn etter godkjenning
-   Test at alt funker i dev
-   Lag en release på master med versjon-bump, beskrivende tittel og oppsummering av endringene dine
-   Publiser release-en for å starte deploy til prod
-   Gå til [packages](https://github.com/navikt/navno-search-admin-api/packages) og hent ```version``` fra siste package (etter at siste release er deployet), og endre siste versjon av ```navnoSearchCommonVersion``` i ```navno-search-api```

## Publisering av felles bibliotek

Ved trigging av workflow, publiseres det et felles bibliotek til maven. Dette brukes også av [navno-search-api](https://github.com/navikt/navno-search-api). Biblioteket inneholder diverse konstanter, samt klassen som brukes for å opprette Opensearch-indexen. Ved deling og versjonering av denne er det mulig å opprette og populere en ny index før man skrur apiet over til å søke mot denne.

## Logging

[Kibana](https://logs.adeo.no/app/discover#/view/ea9b29d0-aa35-11ee-991c-09effcd7b5da)

## Henvendelser

Spørsmål knyttet til koden eller prosjektet kan rettes mot https://github.com/orgs/navikt/teams/personbruker

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #team-personbruker.
