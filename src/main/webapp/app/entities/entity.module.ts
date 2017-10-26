import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { FourchetteIpponCountryModule } from './country/country.module';
import { FourchetteIpponLocationModule } from './location/location.module';
import { FourchetteIpponRestaurantModule } from './restaurant/restaurant.module';
import { FourchetteIpponRatingModule } from './rating/rating.module';
import { FourchetteIpponContributeurModule } from './contributeur/contributeur.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        FourchetteIpponCountryModule,
        FourchetteIpponLocationModule,
        FourchetteIpponRestaurantModule,
        FourchetteIpponRatingModule,
        FourchetteIpponContributeurModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FourchetteIpponEntityModule {}
