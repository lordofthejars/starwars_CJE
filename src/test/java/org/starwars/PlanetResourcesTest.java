package org.starwars;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.starwars.gateway.SwapiGateway;
import org.starwars.service.PlanetService;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlanetResourcesTest {

    @Mock
    SwapiGateway swapiGateway;

    @Mock
    PlanetService planetService;

    @Test
    public void shouldCalculateTheAverageOfRotationOfAllPlanets() {

        final JsonObject planets = Json.createReader(PlanetResourcesTest.class.getResourceAsStream("/planets.json")).readObject();

        when(swapiGateway.getAllPlanets()).thenReturn(planets);
        when(planetService.calculateAverageOfRotationPeriod(planets.getJsonArray("results"))).thenReturn(12.23D);

        PlanetResources planetResources = new PlanetResources();
        planetResources.swapiGateway = swapiGateway;
        planetResources.planetService = planetService;
        planetResources.averageFormatter = new AverageFormatterProducer().averageFormatter();

        final Response response = planetResources.calculateAverageOfRotation();
        assertThat((String)response.getEntity(), is("12.23"));
    }

}
